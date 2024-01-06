package me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend;

import lombok.AccessLevel;
import lombok.Getter;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.smartcorewaterfall.manager.cooldown.CooldownImplementation;
import me.imsergioh.smartcorewaterfall.manager.cooldown.CooldownManager;
import me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend.event.FriendRequestEventHandler;
import me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend.event.FriendResponseEventHandler;
import me.imsergioh.smartcorewaterfall.manager.exception.RedisConnectionNotInitializedException;
import me.imsergioh.smartcorewaterfall.manager.friend.FriendsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Getter
public class FriendCooldownImpl extends CooldownImplementation {

  @Getter(AccessLevel.PROTECTED)
  protected static final long REQUEST_EXPIRATION_MINUTES = 5L;

  private final UUID senderUUID;
  private final UUID receiverUUID;

  private FriendCooldownStatus status = FriendCooldownStatus.PENDING;

  public static String prepareDataDirectory(UUID senderUUID, UUID receiverUUID) {
    return "friend.%s.%s".formatted(senderUUID, receiverUUID);
  }

  protected FriendCooldownImpl(UUID senderUUID, UUID receiverUUID) {
    super(
            System.currentTimeMillis(),
            TimeUnit.MINUTES.toMillis(REQUEST_EXPIRATION_MINUTES),
            prepareDataDirectory(senderUUID, receiverUUID)
    );
    this.senderUUID = senderUUID;
    this.receiverUUID = receiverUUID;
  }

  protected void setStatus(FriendCooldownStatus status) {
    this.status = status;

    FriendCooldownImpl.setStatus(status, this.getSenderUUID(), this.getReceiverUUID());
  }

  @Override
  public void schedule() {
    /* Update required information */
    this.setTimestamp(System.currentTimeMillis());
    super.schedule();

    /* Send redis notification if posible */
    final RedisConnection redisConnection = RedisConnection.mainConnection;
    if (redisConnection == null) {
      return;
    }

    redisConnection.getResource().publish(
            FriendRequestEventHandler.KEY,
            "cooldown.%s".formatted(this.getDataDirectory())
    );
  }

  protected static void responseRequest(FriendCooldownStatus status, UUID senderUUID, UUID receiverUUID) {
    final RedisConnection redisConnection = RedisConnection.mainConnection;
    if (redisConnection == null) {
      return;
    }

    if (status == FriendCooldownStatus.ACCEPTED) {
      final long timestamp = System.currentTimeMillis();

      FriendsManager.addFriend(senderUUID, receiverUUID, false, timestamp);
      FriendsManager.addFriend(receiverUUID, senderUUID, false, timestamp);
    }

    redisConnection.getResource().publish(
            FriendResponseEventHandler.KEY,
            "%s.%s.%s".formatted(status.name(), senderUUID, receiverUUID)
    );
    try {
      CooldownManager.stopCooldowns(prepareDataDirectory(senderUUID, receiverUUID));
    } catch (RedisConnectionNotInitializedException e) {
      e.printStackTrace(System.out);
    }
  }

  /**
   * Si estás utilizando Redis versión 2.8.0 o posterior,
   * el tiempo de expiración original se mantendrá después
   * de un SET subsiguiente sin un nuevo tiempo de expiración.
   * Si estás utilizando una versión anterior, el SET eliminará
   * cualquier tiempo de expiración existente.
   */
  public static void setStatus(FriendCooldownStatus status, UUID senderUUID, UUID receiverUUID) {
    final RedisConnection redisConnection = RedisConnection.mainConnection;
    if (redisConnection == null
            || status == FriendCooldownStatus.PENDING
            || !isPending(senderUUID, receiverUUID)) {
      return;
    }

    if (status == FriendCooldownStatus.ACCEPTED || status == FriendCooldownStatus.DENIED) {
      FriendCooldownImpl.responseRequest(status, senderUUID, receiverUUID);
      return;
    }
    redisConnection.getResource().set(
            "cooldown.%s".formatted(
                    prepareDataDirectory(senderUUID, receiverUUID)
            ), status.name()
    );
  }

  public static boolean isPending(UUID senderUUID, UUID receiverUUID) {
    try {
      final String data = prepareDataDirectory(senderUUID, receiverUUID);
      if (!CooldownManager.hasActiveCooldown(data)) {
        return false;
      }
      final RedisConnection redisConnection = RedisConnection.mainConnection;
      if (redisConnection == null) {
        throw new RedisConnectionNotInitializedException();
      }

      final String value = redisConnection.getResource().get("cooldown.%s".formatted(data));
      return value.equalsIgnoreCase(FriendCooldownStatus.PENDING.name());
    } catch (RedisConnectionNotInitializedException e) {
      e.printStackTrace(System.out);
    }
    return false;
  }

  public static List<IFriendRequest> getFriendRequests(UUID receiverUUID) {
    final List<IFriendRequest> requests = new ArrayList<>();

    try {
      final RedisConnection redisConnection = RedisConnection.mainConnection;
      if (redisConnection == null) {
        throw new RedisConnectionNotInitializedException();
      }

      for (String activeCooldown : CooldownManager.getActiveCooldowns("friend.*.%s".formatted(receiverUUID))) {
        final String[] data = activeCooldown.split("\\.");

        final String value = redisConnection.getResource().get(activeCooldown);
        final FriendCooldownStatus statusEnum = FriendCooldownStatus.valueOf(value);

        final IFriendRequest request = new FriendRequestObject(
                UUID.fromString(data[2]),
                UUID.fromString(data[3])
        );
        request.setStatus(statusEnum);
        requests.add(request);
      }
    } catch (RedisConnectionNotInitializedException
             | IndexOutOfBoundsException e) {
      e.printStackTrace(System.out);
    }

    return requests.stream().filter(Objects::nonNull).collect(Collectors.toList());
  }

  @Override
  public String getIdentification() {
    return this.getStatus().name();
  }

  public static void schedule(UUID senderUUID, UUID receiverUUID) {
    new FriendCooldownImpl(senderUUID, receiverUUID).schedule();
  }
}

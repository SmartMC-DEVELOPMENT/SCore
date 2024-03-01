package me.imsergioh.smartcorewaterfall.manager.friend;

import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.smartcorewaterfall.instance.PlayerLanguages;
import me.imsergioh.smartcorewaterfall.instance.friend.PlayerFriends;
import me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend.FriendCooldownImpl;
import me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend.FriendCooldownStatus;
import me.imsergioh.smartcorewaterfall.manager.cooldown.implementation.friend.IFriendRequest;
import me.imsergioh.smartcorewaterfall.messages.FriendManagerMessages;
import me.imsergioh.smartcorewaterfall.util.AsyncUtilities;
import me.imsergioh.smartcorewaterfall.util.DebugUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FriendsManager {

  public static CompletableFuture<Void> addFriend(UUID playerUuid, UUID friendUuid, boolean best, long timestamp) {
    return AsyncUtilities.schedule(() -> {
      try {
        final var friends = getFriends(playerUuid).get();
        friends.addFriendRelation(friendUuid, best, timestamp);
      } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public static CompletableFuture<Void> removeFriend(UUID playerUuid, UUID friendUuid) {
    return AsyncUtilities.schedule(() -> {
      try {
        final var friends = getFriends(playerUuid).get();
        friends.removeFriendRelation(friendUuid);
      } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public static CompletableFuture<PlayerFriends> getFriends(UUID playerUuid) {
    final var redisCache = "cache:friend:%s".formatted(playerUuid);
    final RedisConnection redisConnection = RedisConnection.mainConnection;

    if (redisConnection != null && redisConnection.getResource().exists(redisCache)) {
      final String cache = redisConnection.getResource().get(redisCache);
      return CompletableFuture.completedFuture(PlayerFriends.GSON.fromJson(cache, PlayerFriends.class));
    }

    final CompletableFuture<PlayerFriends> friendsFuture = fetchFriends(playerUuid);
    friendsFuture.whenCompleteAsync(
            (friend, throwable) -> {
              if (throwable != null) {
                throwablthrow new RuntimeException(e);
                return;
              }

              if (redisConnection != null && friend != null) {
                redisConnection.getResource().psetex(
                        redisCache,
                        TimeUnit.MINUTES.toMillis(10),
                        PlayerFriends.GSON.toJsonTree(friend).getAsString()
                );
              }
            }
    );
    return friendsFuture;
  }

  private static CompletableFuture<PlayerFriends> fetchFriends(UUID playerUuid) {
    return AsyncUtilities.provide(() -> {
      final PlayerFriends friends = new PlayerFriends(playerUuid);
      friends.loadDocument();
      return friends;
    });
  }

  public static void joinFriendStatusCheck(ProxiedPlayer player) {
    AsyncUtilities.schedule(() -> {
      final List<IFriendRequest> friendRequests = FriendsManager.getFriendRequests(
              player.getUniqueId(),
              FriendCooldownStatus.PENDING
      );
      if (friendRequests.isEmpty() || !player.isConnected()) {
        return;
      }
      PlayerLanguages.sendMessage(player, FriendManagerMessages.NAME,
              "join_pending",
              friendRequests.size(),
              player.getName()
      );
    });
  }

  /* Cooldown Implementation */
  public static void scheduleFriendRequest(UUID playerUuid, UUID friendUuid) {
    DebugUtil.debug("friends", "scheduleFriendRequest");
    FriendCooldownImpl.schedule(playerUuid, friendUuid);
  }

  public static void setStatusFriendRequest(UUID senderUuid, UUID receiverUuid, FriendCooldownStatus status) {
    FriendCooldownImpl.setStatus(status, senderUuid, receiverUuid);
  }

  public static boolean isRequestPending(UUID senderUuid, UUID receiverUuid) {
    return FriendCooldownImpl.isPending(senderUuid, receiverUuid);
  }

  public static List<IFriendRequest> getFriendRequests(UUID receiverUUID, FriendCooldownStatus status) {
    return FriendCooldownImpl.getFriendRequests(receiverUUID).stream()
            .filter(request -> request.getStatus() == status)
            .collect(Collectors.toList());
  }

  public static List<IFriendRequest> getFriendRequests(UUID receiverUUID) {
    return FriendCooldownImpl.getFriendRequests(receiverUUID);
  }
}

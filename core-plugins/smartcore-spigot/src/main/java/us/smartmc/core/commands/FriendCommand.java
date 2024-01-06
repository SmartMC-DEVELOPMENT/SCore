package us.smartmc.core.commands;

import me.imsergioh.pluginsapi.connection.RedisConnection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.core.instance.player.friend.PlayerFriends;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class FriendCommand implements CommandExecutor {

  private static final String FRIEND_RESPONSE_KEY = "cooldown.friend.response";

  @Override
  public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
    if (!(commandSender instanceof Player player)) {
      return false;
    }
    if (args.length != 2) {
      return false;
    }

    final String targetName = args[1];

    final @Nullable SmartCorePlayer smartCorePlayer = SmartCorePlayer.get(targetName);
    if (smartCorePlayer == null) {
      return false;
    }
    final UUID targetUuid = smartCorePlayer.getUUID();

    final String answer = args[0];
    FriendRequestAnswer friendRequestAnswer = null;

    for (final FriendRequestAnswer value : FriendRequestAnswer.values()) {
      if (answer.equalsIgnoreCase(value.name())) {
        friendRequestAnswer = value;
        break;
      }
    }
    if (friendRequestAnswer == null) {
      return false;
    }
    
    answerRequest(player.getUniqueId(), targetUuid, friendRequestAnswer);
    return true;
  }

  private static boolean isPendingRequest(UUID senderUuid, UUID receiverUuid) {
    final RedisConnection redisConnection = RedisConnection.mainConnection;
    if (redisConnection == null) {
      return false;
    }

    final String redisCache = "cooldown.friend.%s.%s".formatted(senderUuid, receiverUuid);
    if (!redisConnection.getResource().exists(redisCache)) {
      return false;
    }
    final String data = redisConnection.getResource().get(redisCache);
    return data.equalsIgnoreCase(FriendRequestAnswer.PENDING.name());
  }

  private static boolean answerRequest(UUID senderUuid, UUID receiverUuid, FriendRequestAnswer answer) {
    final RedisConnection redisConnection = RedisConnection.mainConnection;
    if (redisConnection == null) {
      return false;
    }

    if (!isPendingRequest(senderUuid, receiverUuid)) {
      return false;
    }

    final String redisCache = "cooldown.friend.%s.%s".formatted(senderUuid, receiverUuid);
    redisConnection.getResource().set(redisCache, answer.name());

    final long timestamp = System.currentTimeMillis();

    if (answer.equals(FriendRequestAnswer.ACCEPTED)) {
      CompletableFuture.runAsync(() -> {
        final PlayerFriends senderFriends = new PlayerFriends(senderUuid);
        senderFriends.loadDocument();
        senderFriends.addFriendRelation(receiverUuid, false, timestamp);

        final PlayerFriends receiverFriends = new PlayerFriends(receiverUuid);
        receiverFriends.loadDocument();
        receiverFriends.addFriendRelation(senderUuid, false, timestamp);
      }, Executors.newCachedThreadPool());
    }

    redisConnection.getResource().publish(
            FRIEND_RESPONSE_KEY,
            "%s.%s.%s".formatted(answer.name(), senderUuid, receiverUuid)
    );
    return true;
  }

  private enum FriendRequestAnswer {
    ACCEPTED,
    REMOVED,
    PENDING,
    DENIED;
  }
}

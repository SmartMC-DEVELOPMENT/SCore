package me.imsergioh.smartcorewaterfall.command.friend;

import me.imsergioh.smartcorewaterfall.instance.CoreCommand;
import me.imsergioh.smartcorewaterfall.instance.OfflinePlayerData;
import me.imsergioh.smartcorewaterfall.instance.friend.PlayerFriends;
import me.imsergioh.smartcorewaterfall.manager.friend.FriendsManager;
import me.imsergioh.smartcorewaterfall.util.AsyncUtilities;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.ExecutionException;

public class FriendCommand extends CoreCommand {

  public FriendCommand() {
    super("friend");
  }

  /**
   * /friends add <player> - Add a friend to the player.
   * /friends remove <player> - Remove a friend from the player.
   * /friends accept <player> - Accept a friend request.
   * /friends deny <player> - Deny a friend request.
   * /friends list - List all friends.
   * /friends best - List all best friends.
   * /friends online - List all online friends.
   *
   * @param sender
   * @param args
   */
  @Override
  public void execute(CommandSender sender, String[] args) {
    if (args.length == 0) {
      sender.sendMessage(TextComponent.fromLegacyText("You didn't provide the subcommand."));
      return;
    }
    if (!(sender instanceof ProxiedPlayer player)) {
      sender.sendMessage(TextComponent.fromLegacyText("Only players can execute this command."));
      return;
    }
    final String subCommand = args[0];

    AsyncUtilities.schedule(() -> {
      switch (subCommand) {
        case "add" -> {
          if (args.length != 2) {
            sender.sendMessage(TextComponent.fromLegacyText("You have provided invalid argument amount."));
            return;
          }
          final String friendName = args[1];

          final OfflinePlayerData friend;
          try {
            friend = OfflinePlayerData.get(friendName);
          } catch (Exception e) {
            sender.sendMessage(TextComponent.fromLegacyText("This player has never joined the server."));
            return;
          }

          final PlayerFriends friends;
          try {
            friends = FriendsManager.getFriends(player.getUniqueId()).get();
          } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace(System.out);
            return;
          }

          if (friends.getFriendList().containsKey(friend.getUUID())) {
            sender.sendMessage(TextComponent.fromLegacyText("This player is already your friend."));
            return;
          }
          if (FriendsManager.isRequestPending(player.getUniqueId(), friend.getUUID())) {
            sender.sendMessage(TextComponent.fromLegacyText("You already sent a friend request to this player."));
            return;
          }

          FriendsManager.scheduleFriendRequest(player.getUniqueId(), friend.getUUID());
          sender.sendMessage(TextComponent.fromLegacyText("Friend request sent to " + friendName + "."));
        }
        case "accept" -> {
          if (args.length != 2) {
            sender.sendMessage(TextComponent.fromLegacyText("You have provided invalid argument amount."));
            return;
          }
          final String friendName = args[1];

          
        }
        default -> sender.sendMessage(TextComponent.fromLegacyText("Invalid subcommand."));
      }
    });
  }
}

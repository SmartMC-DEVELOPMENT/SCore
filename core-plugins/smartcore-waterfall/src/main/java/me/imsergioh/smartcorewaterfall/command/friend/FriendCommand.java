package me.imsergioh.smartcorewaterfall.command.friend;

import lombok.NonNull;
import me.imsergioh.pluginsapi.handler.LanguagesHandler;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.smartcorewaterfall.instance.CoreCommand;
import me.imsergioh.smartcorewaterfall.instance.OfflinePlayerData;
import me.imsergioh.smartcorewaterfall.instance.PlayerLanguages;
import me.imsergioh.smartcorewaterfall.instance.friend.PlayerFriends;
import me.imsergioh.smartcorewaterfall.manager.friend.FriendsManager;
import me.imsergioh.smartcorewaterfall.messages.FriendManagerMessages;
import me.imsergioh.smartcorewaterfall.messages.ProxyMainMessages;
import me.imsergioh.smartcorewaterfall.util.AsyncUtilities;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.ExecutionException;

public class FriendCommand extends CoreCommand {

  public FriendCommand() {
    super("friend", "smart.friends.command", "f");
  }

  private static void sendCommonTranslatable(
          final @NonNull CommandSender commandSender,
          final @NonNull String identifier,
          final @NonNull String holder,
          final Object... args
  ) {
    if (commandSender instanceof ProxiedPlayer player) {
      PlayerLanguages.sendMessage(player, holder, identifier, args);
      return;
    }
    final String message = LanguagesHandler.get(Language.EN).get(holder).getString(identifier);
    commandSender.sendMessage(TextComponent.fromLegacyText(ChatUtil.parse(message, args)));
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
      FriendCommand.sendCommonTranslatable(sender, "invalid_subcommand", ProxyMainMessages.NAME);
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
          if (args.length < 2) {
            FriendCommand.sendCommonTranslatable(sender, "few_arguments_command_error", ProxyMainMessages.NAME);
            return;
          }
          if (args.length > 2) {
            FriendCommand.sendCommonTranslatable(sender, "too_much_arguments_command_error", ProxyMainMessages.NAME);
            return;
          }
          final String friendName = args[1];

          final OfflinePlayerData friend;
          try {
            friend = OfflinePlayerData.get(friendName);
          } catch (Exception ignored) {
            FriendCommand.sendCommonTranslatable(sender, "no_player_error", ProxyMainMessages.NAME);
            return;
          }

          if (friend.getUUID().equals(player.getUniqueId())) {
            FriendCommand.sendCommonTranslatable(sender, "self_request_error", FriendManagerMessages.NAME);
            return;
          }

          final PlayerFriends friends;
          try {
            friends = FriendsManager.getFriends(player.getUniqueId()).get();
          } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
            return;
          }

          if (friends.getFriendList().containsKey(friend.getUUID())) {
            FriendCommand.sendCommonTranslatable(sender, "already_friend_error", FriendManagerMessages.NAME);
            return;
          }
          if (FriendsManager.isRequestPending(player.getUniqueId(), friend.getUUID())) {
            FriendCommand.sendCommonTranslatable(sender, "already_requested_error", FriendManagerMessages.NAME);
            return;
          }

          FriendsManager.scheduleFriendRequest(player.getUniqueId(), friend.getUUID());
          FriendCommand.sendCommonTranslatable(sender, "request_sent", FriendManagerMessages.NAME, friend.getName());
        }
        /*
        case "accept", "deny", "ignore" -> {
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

          if (!FriendsManager.isRequestPending(player.getUniqueId(), friend.getUUID())) {
            sender.sendMessage(TextComponent.fromLegacyText("The player didn't send you a friend request."));
            return;
          }

          final FriendCooldownStatus status = subCommand.equals("accept")
                  ? FriendCooldownStatus.ACCEPTED
                  : subCommand.equals("deny")
                  ? FriendCooldownStatus.DENIED
                  : FriendCooldownStatus.REMOVED;
          FriendsManager.setStatusFriendRequest(friend.getUUID(), player.getUniqueId(), status);
        }
         */
        default -> FriendCommand.sendCommonTranslatable(sender, "invalid_subcommand", ProxyMainMessages.NAME);
      }
    });
  }
}

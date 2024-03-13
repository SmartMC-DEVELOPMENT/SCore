package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import us.smartmc.smartcore.smartcorevelocity.manager.CustomCommandsManager;

public class CustomCommandsListeners {

    @Subscribe(order = PostOrder.LATE)
    public void onChatPlayer(CommandExecuteEvent event) {
        if (!event.getResult().isAllowed()) return;
        CustomCommandsManager.forEach(manager -> {
            boolean executed =
                    manager.execute(event.getCommandSource(), event.getCommand());
            if (executed) {
                event.setResult(CommandExecuteEvent.CommandResult.denied());
            }
        });
    }

    @Subscribe(order = PostOrder.LAST)
    public void onCommand(CommandExecuteEvent event) {
        CommandSource source = event.getCommandSource();
        if (!(source instanceof Player player)) return;
        CommandExecuteEvent.CommandResult result =
                LoginMessagingListeners.isLoggedIn(player) ?
                        CommandExecuteEvent.CommandResult.allowed() :
                        CommandExecuteEvent.CommandResult.denied();
        if (event.getCommand().startsWith("login") || event.getCommand().startsWith("register")) return;
        event.setResult(result);
    }

    @Subscribe(order = PostOrder.LAST)
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerChatEvent.ChatResult result =
                LoginMessagingListeners.isLoggedIn(player) ?
                PlayerChatEvent.ChatResult.allowed() :
                PlayerChatEvent.ChatResult.denied();
        event.setResult(result);
    }
}

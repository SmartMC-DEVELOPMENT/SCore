package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;

public class LoginListeners {

    @Subscribe
    public void unregisterLoggedIn(DisconnectEvent event) {
        LoginMessageHandler.unregisterLoggedIn(event.getPlayer());
    }

    @Subscribe(order = PostOrder.FIRST)
    public void cancelCommandsIfNotLoggedIn(CommandExecuteEvent event) {
        CommandSource source = event.getCommandSource();
        if (!(source instanceof Player player)) return;
        CommandExecuteEvent.CommandResult result =
                LoginMessageHandler.isLoggedIn(player) ?
                        CommandExecuteEvent.CommandResult.allowed() :
                        CommandExecuteEvent.CommandResult.denied();
        if (event.getCommand().startsWith("login") || event.getCommand().startsWith("register") || event.getCommand().startsWith("premium")) return;
        event.setResult(result);
    }

    @Subscribe(order = PostOrder.FIRST)
    public void cancelChatIfNotLoggedIn(PlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerChatEvent.ChatResult result =
                LoginMessageHandler.isLoggedIn(player) ?
                        PlayerChatEvent.ChatResult.allowed() :
                        PlayerChatEvent.ChatResult.denied();
        event.setResult(result);
    }

}

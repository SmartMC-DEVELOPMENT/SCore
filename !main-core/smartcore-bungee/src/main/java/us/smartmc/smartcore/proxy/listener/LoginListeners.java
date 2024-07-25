package us.smartmc.smartcore.proxy.listener;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginListeners implements Listener {

    @EventHandler
    public void unregisterLoggedIn(PlayerDisconnectEvent event) {
        LoginMessageHandler.unregisterLoggedIn(event.getPlayer());
    }

    @EventHandler(priority = -99)
    public void cancelCommandsIfNotLoggedIn(ChatEvent event) {
        String label = event.getMessage();
        if (!label.startsWith("/")) return;
        CommandSender sender = (CommandSender) event.getSender();
        if (!(sender instanceof ProxiedPlayer player)) return;

        boolean allow = LoginMessageHandler.isLoggedIn(player);
        if (event.getMessage().startsWith("/login") || event.getMessage().startsWith("/register") || event.getMessage().startsWith("/premium")) return;
        if (!allow) {
            event.setCancelled(true);
            System.out.println("Cancelled by cancelCommandsIfNotLoggedIn 1");
        }
    }

    @EventHandler(priority = -1)
    public void cancelChatIfNotLoggedIn(ChatEvent event) {
        if (event.getMessage().startsWith("/")) return;
        if (!(event.getSender() instanceof ProxiedPlayer player)) return;

        boolean allowed = LoginMessageHandler.isLoggedIn(player);
        if (!allowed) {
            event.setCancelled(true);
            System.out.println("Cancelled by cancelChatIfNotLoggedIn");
        }
    }

}

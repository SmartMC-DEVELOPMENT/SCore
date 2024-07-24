package us.smartmc.smartcore.proxy.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import us.smartmc.smartcore.proxy.manager.CustomCommandsManager;

public class CustomCommandsListeners implements Listener {

    @EventHandler(priority = -1)
    public void onChatPlayer(ChatEvent event) {
        String label = event.getMessage();
        if (!label.startsWith("/")) return;
        if (event.isCancelled()) return;

        if (!(event.getSender() instanceof ProxiedPlayer player)) return;

        for (CustomCommandsManager manager : CustomCommandsManager.getManagers()) {
            boolean executed =
                    manager.execute(player, label);
            event.setCancelled(executed);
        }
    }
}

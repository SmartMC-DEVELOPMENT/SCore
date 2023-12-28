package me.imsergioh.smartcorewaterfall.listener;

import me.imsergioh.smartcorewaterfall.manager.CustomCommandsManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class CustomCommandsListeners implements Listener {

    @EventHandler
    public void onChatPlayer(ChatEvent event) {
        String message = event.getMessage();
        CustomCommandsManager.forEach(manager -> {
            boolean executed =
                    manager.execute((CommandSender) event.getSender(), message);
            event.setCancelled(executed);
        });
    }
}

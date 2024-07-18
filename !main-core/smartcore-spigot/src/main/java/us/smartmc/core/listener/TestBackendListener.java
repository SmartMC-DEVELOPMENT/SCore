package us.smartmc.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.smartmc.core.SmartCore;

public class TestBackendListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChatEvent(AsyncPlayerChatEvent event) {
        SmartCore.getPlugin().getBackendClient().broadcastCommand("chat@spigot-core",
                "chat " + event.getPlayer().getName() + " " + event.getMessage());
    }
}

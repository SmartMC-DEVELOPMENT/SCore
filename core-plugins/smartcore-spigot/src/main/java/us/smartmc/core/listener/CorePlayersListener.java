package us.smartmc.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.core.instance.player.SmartCorePlayer;

public class CorePlayersListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void registerSmartCorePlayer(PlayerJoinEvent event)  {
        SmartCorePlayer.register(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDisconnect(PlayerQuitEvent event) {
        SmartCorePlayer.unload(event.getPlayer());
    }
}

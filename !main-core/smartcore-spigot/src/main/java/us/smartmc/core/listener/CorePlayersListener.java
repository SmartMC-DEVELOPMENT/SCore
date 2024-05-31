package us.smartmc.core.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.core.util.PlayerVersions;

public class CorePlayersListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void registerSmartCorePlayer(PlayerJoinEvent event)  {
        Player player = event.getPlayer();
        player.getScoreboard().getTeams().clear();
        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        SmartCorePlayer.register(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDisconnect(PlayerQuitEvent event) {
        SmartCorePlayer.unload(event.getPlayer());
        PlayerVersions.unregister(event.getPlayer().getUniqueId());
    }
}

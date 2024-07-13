package us.smartmc.game.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.core.SmartCore;
import us.smartmc.game.SkyBlockPlugin;

public class BackendBukkitListeners implements Listener {

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(SmartCore.getPlugin(), this::sendCount);
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTaskAsynchronously(SmartCore.getPlugin(), this::sendCount);
    }

    private void sendCount() {
        SkyBlockPlugin.sendBackendCommand(
                "skyblock setonline",
                SmartCore.getServerId(),
                String.valueOf(Bukkit.getOnlinePlayers().size()));
    }

}

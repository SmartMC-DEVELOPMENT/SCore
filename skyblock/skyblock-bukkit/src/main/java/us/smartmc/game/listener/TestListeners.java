package us.smartmc.game.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import us.smartmc.game.SkyBlockPlugin;
import us.smartmc.game.instance.SkyBlockPlayer;

public class TestListeners implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void testCoins(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(SkyBlockPlugin.getPlugin(), () -> {
            SkyBlockPlayer.getOrCreate(event.getPlayer()).addCoins(2);
        }, 10);
    }
}

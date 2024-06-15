package us.smartmc.game.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import us.smartmc.game.instance.SkyBlockPlayer;

public class TestListeners implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void testCoins(PlayerJoinEvent event) {
        SkyBlockPlayer.getOrCreate(event.getPlayer()).addCoins(2);
    }
}

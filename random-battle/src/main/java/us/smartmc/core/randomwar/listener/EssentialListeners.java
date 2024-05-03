package us.smartmc.core.randomwar.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.core.randomwar.RandomWar;

public class EssentialListeners implements Listener {

    private static final RandomWar plugin = RandomWar.getPlugin();

    @EventHandler
    public void removeJoinMessage(PlayerJoinEvent event) {
        event.joinMessage(null);
    }

    @EventHandler
    public void removeQuitMessage(PlayerQuitEvent event) {
        event.quitMessage(null);
    }
}

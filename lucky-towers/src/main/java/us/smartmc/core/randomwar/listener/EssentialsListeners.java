package us.smartmc.core.randomwar.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.core.randomwar.RandomBattle;
import us.smartmc.core.randomwar.instance.player.GamePlayer;

public class EssentialsListeners implements Listener {

    private static final RandomBattle plugin = RandomBattle.getPlugin();

    @EventHandler
    public void removeJoinMessage(PlayerJoinEvent event) {
        event.joinMessage(null);
    }

    @EventHandler
    public void removeQuitMessage(PlayerQuitEvent event) {
        event.quitMessage(null);
    }

    @EventHandler
    public void createGamePlayer(PlayerJoinEvent event) {
        GamePlayer.get(event.getPlayer().getUniqueId());
    }
}

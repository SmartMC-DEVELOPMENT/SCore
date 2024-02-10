package us.smartmc.gamesmanager.gamesmanagerspigot.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GamePlayerManager;

import java.util.UUID;

public class PlayerListeners implements Listener {

    private final GamePlayerManager<?> playerManager;

    public PlayerListeners(GamePlayerManager<?> playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        playerManager.register(playerManager.createGamePlayer(uuid));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        playerManager.unregister(uuid);
    }
}

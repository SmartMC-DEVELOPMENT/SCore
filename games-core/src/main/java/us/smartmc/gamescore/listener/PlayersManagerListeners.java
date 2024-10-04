package us.smartmc.gamescore.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.gamescore.event.player.GamePlayerJoinEvent;
import us.smartmc.gamescore.event.player.GamePlayerQuitEvent;
import us.smartmc.gamescore.instance.exception.NonExistentManagerExpection;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.manager.player.PlayersManager;
import us.smartmc.gamescore.util.BukkitUtil;

public class PlayersManagerListeners implements Listener {

    private static final PlayersManager playersManager = MapManager.getManager(PlayersManager.class);

    @EventHandler
    public void registerPlayer(PlayerJoinEvent event) throws NonExistentManagerExpection {
        Player player = event.getPlayer();
        if (playersManager == null) throw new NonExistentManagerExpection(PlayersManager.class);
        GameCorePlayer gameCorePlayer = playersManager.register(player.getUniqueId());
        BukkitUtil.callEvent(new GamePlayerJoinEvent(gameCorePlayer));
    }

    @EventHandler
    public void unregisterPlayer(PlayerQuitEvent event) throws NonExistentManagerExpection {
        Player player = event.getPlayer();
        if (playersManager == null) throw new NonExistentManagerExpection(PlayersManager.class);
        GameCorePlayer gameCorePlayer = playersManager.remove(player.getUniqueId());
        BukkitUtil.callEvent(new GamePlayerQuitEvent(gameCorePlayer));
    }
}

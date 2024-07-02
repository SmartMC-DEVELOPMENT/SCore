package us.smartmc.game.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.game.event.SkyBlockPlayerDataLoadedEvent;
import us.smartmc.game.instance.SkyBlockPlayer;
import us.smartmc.game.instance.SkyBlockPlayerIsland;
import us.smartmc.skyblock.manager.IslandsManager;

import java.util.UUID;

public class MainIslandsListeners  implements Listener {

    @EventHandler
    public void loadOrCreateIsland(SkyBlockPlayerDataLoadedEvent event) {
        Player player = event.getSkyBlockPlayer().getBukkitPlayer();
        UUID islandId = event.getData().getIslandSetId();
        boolean createIsland = islandId == null;

        SkyBlockPlayerIsland island = createIsland ? new SkyBlockPlayerIsland(player.getUniqueId()) : new SkyBlockPlayerIsland(islandId, player.getUniqueId());
        IslandsManager.register(island);

        // If created then set
        if (createIsland) {
            event.getData().setIslandSet(island);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void unloadIsland(PlayerQuitEvent event) {
        SkyBlockPlayer skyBlockPlayer = SkyBlockPlayer.get(event.getPlayer());
        UUID islandId = skyBlockPlayer.getPlayerData().getIslandSetId();
        if (islandId == null) return;
        IslandsManager.unregister(islandId);
    }
}

package us.smartmc.game.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import us.smartmc.game.SkyBlockPlugin;
import us.smartmc.game.event.SkyBlockPlayerDataLoadedEvent;
import us.smartmc.game.instance.SkyBlockPlayer;
import us.smartmc.game.instance.SkyBlockPlayerIsland;
import us.smartmc.game.util.WorldUtils;
import us.smartmc.skyblock.instance.island.ISkyBlockIsland;
import us.smartmc.skyblock.manager.IslandsManager;

import java.util.UUID;

public class MainIslandsListeners  implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void worldInit(org.bukkit.event.world.WorldInitEvent event) {
        World world = event.getWorld();
        if (!world.getName().startsWith("island-")) return;
        world.setKeepSpawnInMemory(false);
    }

    @EventHandler
    public void loadChunks(WorldLoadEvent event) {
        World world = event.getWorld();
        if (!world.getName().startsWith("island-")) return;

        UUID islandId = UUID.fromString(world.getName().replace("island-", ""));
        ISkyBlockIsland island = IslandsManager.get(islandId);

        if(!(island instanceof SkyBlockPlayerIsland playerIsland)) return;

        Bukkit.getScheduler().runTask(SkyBlockPlugin.getPlugin(), () -> {
            Location pos1 = playerIsland.getIslandData().getMaxLocationBukkit(world);
            Location pos2 = playerIsland.getIslandData().getMinLocationBukkit(world);
            WorldUtils.loadChunksBetweenLocations(pos1, pos2);
        });
    }

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

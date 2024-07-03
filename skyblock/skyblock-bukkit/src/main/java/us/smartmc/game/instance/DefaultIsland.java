package us.smartmc.game.instance;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import us.smartmc.game.SkyBlockPlugin;
import us.smartmc.game.manager.IslandsSchematicsManager;

import java.util.UUID;

public class DefaultIsland extends SkyBlockPlayerIsland {



    public DefaultIsland(UUID islandId) {
        super(islandId, null);
    }

    @Override
    public void register() {
        Bukkit.getScheduler().runTask(SkyBlockPlugin.getPlugin(), () -> {
            islandData = new SkyBlockPlayerIslandData(this);
            if (islandData.hasIslandReference()) return;
            Bukkit.getScheduler().runTask(SkyBlockPlugin.getPlugin(), this::setupIsland);
        });
    }

    @Override
    public void setupIsland() {
        World world = createIslandWorld();
        world.setSpawnLocation(0, 70, 0);
        Location spawn = world.getSpawnLocation();
        Location min = spawn.clone().add(-30, -30, -30);
        Location max = spawn.clone().add(30, 30, 30);
        islandData.setupIslandData(min, max, spawn);
        Bukkit.getScheduler().runTaskAsynchronously(SkyBlockPlugin.getPlugin(), () -> {
            // Load from island set or default one if not set!
            UUID islandIdToGenerate = IslandsSchematicsManager.getDefaultIslandId();
            IslandsSchematicsManager.loadAndPasteSchematic(world, islandIdToGenerate);
        });
    }

    public World getWorld() {
        return Bukkit.getWorld(getIslandWorldName(islandId));
    }

}


package us.smartmc.game.instance;

import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import lombok.Getter;
import lombok.Setter;
import me.imsergioh.pluginsapi.region.BukkitCuboidRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import us.smartmc.game.SkyBlockPlugin;
import us.smartmc.game.manager.IslandsSchematicsManager;
import us.smartmc.game.util.WorldUtils;
import us.smartmc.skyblock.instance.island.ISkyBlockIsland;

import java.util.UUID;

@Getter
public class SkyBlockPlayerIsland implements ISkyBlockIsland {

    protected final UUID islandId;
    private final UUID ownerId;

    protected SkyBlockPlayerIslandData islandData;

    boolean createdIsland;

    @Setter
    @Getter
    private String islandWorldName;
    private BukkitCuboidRegion cuboidRegion;

    // Create Island
    public SkyBlockPlayerIsland(UUID ownerId) {
        this.islandId = UUID.randomUUID();
        this.ownerId = ownerId;
        createdIsland = true;
    }

    // Load Island
    public SkyBlockPlayerIsland(UUID islandId, UUID ownerId) {
        this.islandId = islandId;
        this.ownerId = ownerId;
    }

    // When loads from manager
    @Override
    public void register() {
        // Load Island data (DB info)
        Bukkit.getScheduler().runTask(SkyBlockPlugin.getPlugin(), () -> {
            islandData = new SkyBlockPlayerIslandData(this);
            Bukkit.getScheduler().runTask(SkyBlockPlugin.getPlugin(), this::setupIsland);
        });
    }

    public synchronized BukkitCuboidRegion getCuboidRegion() {
        if (cuboidRegion == null) {
            World world = (World) getWorld();
            cuboidRegion = new BukkitCuboidRegion(world.getName(), islandData.getMinLocationBukkit(world), islandData.getMaxLocationBukkit(world));
        }
        return cuboidRegion;
    }

    public void setupIsland() {
        try {
            SlimeWorld world = IslandsSchematicsManager.createIslandWorld(islandId);
            islandWorldName = world.getName();
            Location spawn = new Location(
                    Bukkit.getWorld(islandWorldName),
                    world.getPropertyMap().getValue(SlimeProperties.SPAWN_X),
                    world.getPropertyMap().getValue(SlimeProperties.SPAWN_Y),
                    world.getPropertyMap().getValue(SlimeProperties.SPAWN_Z));
            Location min = spawn.clone().add(-30, -30, -30);
            Location max = spawn.clone().add(30, 30, 30);
            islandData.setupIslandData(min, max, spawn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // When unloads from manager
    @Override
    public void unregister() {
        islandData.saveData();
        try {
            Bukkit.unloadWorld(islandWorldName, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String displayName() {
        return null;
    }

    public SkyBlockPlayer getSkyBlockPlayer() {
        return SkyBlockPlayer.get(ownerId);
    }

    @Override
    public UUID getId() {
        return islandId;
    }

    private SlimeWorld getWorld() {
        return WorldUtils.getSlimePlugin().getWorld(islandWorldName);
    }

}

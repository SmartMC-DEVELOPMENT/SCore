package us.smartmc.game.manager;

import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.api.world.properties.SlimeProperties;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;

import us.smartmc.game.instance.DefaultIsland;
import us.smartmc.game.instance.SkyBlockPlayerIsland;
import us.smartmc.game.util.WorldUtils;
import us.smartmc.skyblock.manager.IslandsManager;

import java.io.File;
import java.util.UUID;

public class IslandsSchematicsManager {

    private static final File ISLANDS_DIRECTORY = new File("/home/data/skyblock/islands");

    @Getter
    private static final UUID defaultIslandId = UUID.fromString("e3d71b5a-2f63-4084-abe4-f03cb33258bd");

    public static void registerDefaults() {
        ISLANDS_DIRECTORY.mkdirs();
        IslandsManager.register(new DefaultIsland(defaultIslandId));
    }

    public static File getMapSchematicFile(UUID islandId) {
        return new File(ISLANDS_DIRECTORY, islandId.toString() + ".schem");
    }

    private static Location getSpawnLocation(World world, UUID islandId) {
        SkyBlockPlayerIsland island = (SkyBlockPlayerIsland) us.smartmc.skyblock.manager.IslandsManager.get(islandId);
        return island.getIslandData().getSpawnLocation(world);
    }

    public static SlimeWorld createIslandWorld(UUID id) throws Exception {
        return getSlimeWorld(id);
    }

    private static SlimeWorld getSlimeWorld(UUID id) throws Exception {
        SlimePropertyMap properties = new SlimePropertyMap();
        properties.setValue(SlimeProperties.DIFFICULTY, "NORMAL");
        properties.setValue(SlimeProperties.SPAWN_X, 0);
        properties.setValue(SlimeProperties.SPAWN_Y, 100);
        properties.setValue(SlimeProperties.SPAWN_Z, 0);
        return WorldUtils.getSlimePlugin().loadWorld(WorldUtils.getMainLoader(), getIslandWorldName(id), true, properties);
    }

    public static SlimeWorld getDefaultIslandWorld() throws Exception {
        return getSlimeWorld(defaultIslandId);
    }

    protected static String getIslandWorldName(UUID id) {
        return "island-" + id.toString();
    }

}

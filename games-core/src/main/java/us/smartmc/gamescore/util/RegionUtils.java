package us.smartmc.gamescore.util;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;
import org.joml.Vector3i;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboidRegion;
import us.smartmc.gamescore.instance.cuboid.CuboidRegion;
import us.smartmc.gamescore.instance.serialization.BlockStateWrapper;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;
import us.smartmc.gamescore.manager.RegionsManager;

import java.util.*;
import java.util.function.Consumer;

public class RegionUtils {

    public static CuboidRegion getFirstRegionByLocation(Location location) {
        RegionsManager regionsManager = RegionsManager.getManager(RegionsManager.class);
        if (regionsManager == null) return null;
        Vector3i vector3i = new Vector3i(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        for (CuboidRegion region : regionsManager.values()) {
            if (region.getDefaultCuboid().contains(vector3i)) {
                return region;
            }
        }
        return null;
    }

    public static void consumeBlocks(BukkitCuboidRegion region, Consumer<Block> consumer) {
        consumeRegion(region, vec -> {
            Block block = Bukkit.getWorld(Objects.requireNonNull(region.getConfig().getWorld(region.getCuboid().getWorld())).getName()).getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
            consumer.accept(block);
        });
    }

    public static void consumeRegion(BukkitCuboidRegion region, Consumer<BlockVector> consumer) {
        for (Iterator<BlockVector> it = region.iterator(); it.hasNext(); ) {
            BlockVector vec = it.next();
            consumer.accept(vec);
        }
    }
}

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

import java.util.*;
import java.util.function.Consumer;

public class RegionUtils {

    public static void consumeBlocks(BukkitCuboidRegion region, Consumer<Block> consumer) {
        consumeRegion(region, vec -> {
            Block block = Bukkit.getWorld(Objects.requireNonNull(region.getConfig().getWorld(Bukkit.getWorlds().get(0))).getName()).getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
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

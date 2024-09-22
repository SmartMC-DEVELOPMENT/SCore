package us.smartmc.gamescore.util;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public class RegionUtils {

    public static void consumeBlocks(CuboidRegion region, Consumer<Block> consumer) {
        consumeRegion(region, vec -> {
            Block block = Bukkit.getWorld(Objects.requireNonNull(region.getWorld()).getName()).getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
            consumer.accept(block);
        });
    }

    public static void consumeRegion(CuboidRegion region, Consumer<BlockVector> consumer) {
        for (Iterator<BlockVector> it = region.iterator(); it.hasNext(); ) {
            BlockVector vec = it.next();
            consumer.accept(vec);
        }
    }

    public CuboidRegion getCuboidRegion(Location loc1, Location loc2) {
        Vector min = WorldEditUtil.getMinVector(loc1, loc2);
        Vector max = WorldEditUtil.getMaxVector(loc1, loc2);
        return new CuboidRegion(min, max);
    }
}

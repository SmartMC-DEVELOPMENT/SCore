package us.smartmc.gamescore.util;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import me.imsergioh.pluginsapi.util.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.joml.Vector3d;
import us.smartmc.gamescore.instance.serialization.BlockStateWrapper;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public class RegionUtils {

    public static void pasteAtLocation(Location newMin, CuboidWrapper wrapper) {
        Vector3d min = wrapper.getMin();
        int offsetX = newMin.getBlockX() - (int) min.x();
        int offsetY = newMin.getBlockY() - (int) min.y();
        int offsetZ = newMin.getBlockZ() - (int) min.z();

        System.out.println("Offsets: X=" + offsetX + ", Y=" + offsetY + ", Z=" + offsetZ);

        BukkitUtil.runSync(() -> {
            for (BlockStateWrapper blockState : wrapper.getBlocks()) {
                Location blockLocation = new Location(newMin.getWorld(),
                        blockState.getX() + offsetX,
                        blockState.getY() + offsetY,
                        blockState.getZ() + offsetZ);

                System.out.println("Pasting block at: " + blockLocation.toString());

                Block newBlock = blockLocation.getBlock();
                Material material = Material.getMaterial(blockState.getType());

                if (material == null) {
                    System.out.println("WARNING: Material not found for type: " + blockState.getType());
                    continue;
                }

                newBlock.setType(material);
                newBlock.setData(blockState.getTypeData());

                System.out.println("DEBUG BLOCK SET: " + material + " " + LocationSerializer.toString(blockLocation));
            }
        });
    }

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

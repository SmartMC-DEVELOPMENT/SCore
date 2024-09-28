package us.smartmc.gamescore.util;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import me.imsergioh.pluginsapi.util.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Vector3d;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.cuboid.Cuboid;
import us.smartmc.gamescore.instance.serialization.BlockStateWrapper;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;

import java.util.*;
import java.util.function.Consumer;

public class RegionUtils {

    public static void pasteAtLocation(Location newMin, CuboidWrapper wrapper) {
        Vector3d min = wrapper.getMin();
        int offsetX = newMin.getBlockX() - (int) min.x();
        int offsetY = newMin.getBlockY() - (int) min.y();
        int offsetZ = newMin.getBlockZ() - (int) min.z();

        List<BlockStateWrapper> blocks = wrapper.getBlocks();
        int batchSize = 1000;
        int totalBlocks = blocks.size();

        new BukkitRunnable() {
            int index = 0;

            @Override
            public void run() {
                // Procesar un lote de bloques
                for (int i = 0; i < batchSize && index < totalBlocks; i++, index++) {
                    BlockStateWrapper blockState = blocks.get(index);
                    Location blockLocation = new Location(newMin.getWorld(),
                            blockState.getX() + offsetX,
                            blockState.getY() + offsetY,
                            blockState.getZ() + offsetZ);

                    Block newBlock = blockLocation.getBlock();
                    Material material = Material.getMaterial(blockState.getType());

                    if (material != null) {
                        newBlock.setType(material);
                        newBlock.setData(blockState.getTypeData());
                    }
                }

                // Verifica si se han procesado todos los bloques
                if (index >= totalBlocks) {
                    cancel(); // Detener la tarea si se han procesado todos los bloques
                }
            }
        }.runTaskTimer(GamesCoreAPI.getApi().getPlugin(), 0L, 1L); // Cambia YourPluginInstance por tu clase principal
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

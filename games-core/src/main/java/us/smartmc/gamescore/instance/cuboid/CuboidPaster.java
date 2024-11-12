package us.smartmc.gamescore.instance.cuboid;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.joml.Vector3i;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.serialization.BlockStateWrapper;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;

import java.util.*;

public class CuboidPaster {

    private final CuboidWrapper wrapper;

    public CuboidPaster(CuboidWrapper wrapper) {
        this.wrapper = wrapper;
    }

    public Map<Location, BlockStateWrapper> getRelativeLocationsWithMaterials(Location newMin) {
        Map<Location, BlockStateWrapper> data = new HashMap<>();
        Vector3i min = wrapper.getMin();
        int offsetX = newMin.getBlockX() - (int) min.x();
        int offsetY = newMin.getBlockY() - (int) min.y();
        int offsetZ = newMin.getBlockZ() - (int) min.z();

        List<BlockStateWrapper> blocks = wrapper.getBlocks();
        for (BlockStateWrapper blockState : blocks) {
            Location blockLocation = new Location(newMin.getWorld(),
                    blockState.getX() + offsetX,
                    blockState.getY() + offsetY,
                    blockState.getZ() + offsetZ);
            Material material = Material.getMaterial(blockState.getType());
            if (material == Material.AIR) continue;
            data.put(blockLocation, blockState);
        }
        return data;
    }

    public BukkitCuboid pasteAt(Location newMin, Runnable... completedActions) {
        Vector3i min = wrapper.getMin();
        int offsetX = newMin.getBlockX() - (int) min.x();
        int offsetY = newMin.getBlockY() - (int) min.y();
        int offsetZ = newMin.getBlockZ() - (int) min.z();

        List<BlockStateWrapper> blocks = wrapper.getBlocks();
        int batchSize = 4096;
        int totalBlocks = blocks.size();

        // Calcular las coordenadas mínimas y máximas para el cubo
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        int maxZ = Integer.MIN_VALUE;

        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int minZ = Integer.MAX_VALUE;

        // Determinar las coordenadas mínimas y máximas de los bloques
        for (BlockStateWrapper blockState : blocks) {
            int relativeX = blockState.getX() + offsetX;
            int relativeY = blockState.getY() + offsetY;
            int relativeZ = blockState.getZ() + offsetZ;

            minX = Math.min(minX, relativeX);
            maxX = Math.max(maxX, relativeX);
            minY = Math.min(minY, relativeY);
            maxY = Math.max(maxY, relativeY);
            minZ = Math.min(minZ, relativeZ);
            maxZ = Math.max(maxZ, relativeZ);
        }

        BukkitCuboid cuboid = new BukkitCuboid(
                new Location(newMin.getWorld(), minX, minY, minZ),
                new Location(newMin.getWorld(), maxX, maxY, maxZ)
        );

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
                    }
                }

                // Verifica si se han procesado todos los bloques
                if (index >= totalBlocks) {
                    // Run all runnables available when completed to paste
                    for (Runnable runnable : completedActions) {
                        runnable.run();
                    }
                    cancel(); // Detener la tarea si se han procesado todos los bloques
                }
            }
        }.runTaskTimer(GamesCoreAPI.getApi().getPlugin(), 0L, 1L); // Cambia YourPluginInstance por tu clase principal

        return cuboid; // Devolver el cubo creado
    }

}

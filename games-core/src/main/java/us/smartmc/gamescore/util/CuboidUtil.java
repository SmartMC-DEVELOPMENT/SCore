package us.smartmc.gamescore.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.joml.Vector3i;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboid;
import us.smartmc.gamescore.instance.cuboid.Cuboid;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CuboidUtil {

    private static final String SERIALIZER_SEPARATOR = " ";

    public static String vectorToString(Vector3i vector3i) {
        return vector3i.x() + SERIALIZER_SEPARATOR +
                vector3i.y() + SERIALIZER_SEPARATOR +
                vector3i.z();
    }

    public static Vector3i stringToVector(String str) {
        String[] parts = str.split(SERIALIZER_SEPARATOR);
        return new Vector3i(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }

    /**
     * Recorre todos los bloques dentro del cuboide y aplica una acción sobre cada bloque.
     *
     * @param cuboid El cuboide que contiene los bloques.
     * @param blockAction La acción a aplicar en cada bloque (Consumer).
     */
    public static void forEachBlock(BukkitCuboid cuboid, Consumer<Block> blockAction) {
        World world = cuboid.getWorld();
        Location min = cuboid.getMinLocation();
        Location max = cuboid.getMaxLocation();

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    Block block = world.getBlockAt(x, y, z);
                    blockAction.accept(block);
                }
            }
        }
    }

    /**
     * Devuelve una lista de todos los bloques dentro del cuboide.
     *
     * @param cuboid El cuboide que contiene los bloques.
     * @return Una lista de bloques dentro del cuboide.
     */
    public static List<Block> getBlocks(World world, Cuboid cuboid) {
        List<Block> blocks = new ArrayList<>();
        Vector3i min = cuboid.getMin();
        Vector3i max = cuboid.getMax();

        for (int x = min.x(); x <= max.x(); x++) {
            for (int y = min.y(); y <= max.y(); y++) {
                for (int z = min.z(); z <= max.z(); z++) {
                    blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }

        return blocks;
    }

    /**
     * Reemplaza todos los bloques dentro del cuboide por un bloque específico.
     *
     * @param cuboid El cuboide que contiene los bloques.
     * @param material El material al que quieres cambiar todos los bloques.
     */
    public static void replaceBlocks(BukkitCuboid cuboid, Material material) {
        forEachBlock(cuboid, block -> block.setType(material));
    }

    /**
     * Cuenta el número de bloques de un material específico dentro del cuboide.
     *
     * @param cuboid El cuboide que contiene los bloques.
     * @param material El material a contar.
     * @return El número de bloques del material especificado.
     */
    public static int countBlocks(BukkitCuboid cuboid, Material material) {
        final int[] count = {0};
        forEachBlock(cuboid, block -> {
            if (block.getType() == material) {
                count[0]++;
            }
        });
        return count[0];
    }

}

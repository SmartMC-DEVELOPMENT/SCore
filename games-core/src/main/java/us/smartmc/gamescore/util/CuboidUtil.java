package us.smartmc.gamescore.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import us.smartmc.gamescore.instance.cuboid.Cuboid;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CuboidUtil {


    /**
     * Recorre todos los bloques dentro del cuboide y aplica una acción sobre cada bloque.
     *
     * @param cuboid El cuboide que contiene los bloques.
     * @param blockAction La acción a aplicar en cada bloque (Consumer).
     */
    public static void forEachBlock(Cuboid cuboid, Consumer<Block> blockAction) {
        World world = cuboid.getMin().getWorld();
        Location min = cuboid.getMin();
        Location max = cuboid.getMax();

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
    public static List<Block> getBlocks(Cuboid cuboid) {
        List<Block> blocks = new ArrayList<>();
        World world = cuboid.getMin().getWorld();
        Location min = cuboid.getMin();
        Location max = cuboid.getMax();

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
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
    public static void replaceBlocks(Cuboid cuboid, Material material) {
        forEachBlock(cuboid, block -> block.setType(material));
    }

    /**
     * Cuenta el número de bloques de un material específico dentro del cuboide.
     *
     * @param cuboid El cuboide que contiene los bloques.
     * @param material El material a contar.
     * @return El número de bloques del material especificado.
     */
    public static int countBlocks(Cuboid cuboid, Material material) {
        final int[] count = {0};
        forEachBlock(cuboid, block -> {
            if (block.getType() == material) {
                count[0]++;
            }
        });
        return count[0];
    }

}

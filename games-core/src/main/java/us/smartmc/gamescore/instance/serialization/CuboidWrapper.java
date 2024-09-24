package us.smartmc.gamescore.instance.serialization;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import us.smartmc.gamescore.instance.cuboid.Cuboid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CuboidWrapper implements Serializable {

    private final List<BlockStateWrapper> blocks;
    private final Location min, max;

    // Constructor que toma un cuboide y serializa los bloques dentro de la región
    public CuboidWrapper(Cuboid cuboid) {
        this.min = cuboid.getMin();
        this.max = cuboid.getMax();

        this.blocks = new ArrayList<>();

        for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
                    Block block = min.getWorld().getBlockAt(x, y, z);
                    // Omitir los bloques de aire para no serializarlos
                    if (block.getType() != Material.AIR) {
                        blocks.add(new BlockStateWrapper(block));
                    }
                }
            }
        }
    }

    // Método para pegar la región en una nueva ubicación
    public void pasteAtLocation(Location newMin) {
        int offsetX = newMin.getBlockX() - min.getBlockX();
        int offsetY = newMin.getBlockY() - min.getBlockY();
        int offsetZ = newMin.getBlockZ() - min.getBlockZ();

        for (BlockStateWrapper blockState : blocks) {
            // Calcula la nueva ubicación del bloque
            Location blockLocation = new Location(newMin.getWorld(),
                    blockState.getX() + offsetX,
                    blockState.getY() + offsetY,
                    blockState.getZ() + offsetZ);
            // Recupera el bloque en la nueva ubicación
            Block newBlock = blockLocation.getBlock();
            // Establece el tipo del bloque en la nueva ubicación
            newBlock.setType(Material.getMaterial(blockState.getType()));
        }
    }

}

package us.smartmc.gamescore.instance.serialization;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.joml.Vector3d;
import us.smartmc.gamescore.instance.cuboid.Cuboid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CuboidWrapper implements Serializable {

    private final List<BlockStateWrapper> blocks;
    private final Vector3d min, max;

    // Constructor que toma un cuboide y serializa los bloques dentro de la región
    public CuboidWrapper(Cuboid cuboid) {
        this.min = new Vector3d(cuboid.getMin().getX(), cuboid.getMin().getY(), cuboid.getMin().getZ());
        this.max = new Vector3d(cuboid.getMax().getX(), cuboid.getMax().getY(), cuboid.getMax().getZ());

        Location minLoc = cuboid.getMin();
        Location maxLoc = cuboid.getMax();

        this.blocks = new ArrayList<>();

        for (int x = minLoc.getBlockX(); x <= maxLoc.getBlockX(); x++) {
            for (int y = minLoc.getBlockY(); y <= maxLoc.getBlockY(); y++) {
                for (int z = minLoc.getBlockZ(); z <= maxLoc.getBlockZ(); z++) {
                    Block block = minLoc.getWorld().getBlockAt(x, y, z);
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
        int offsetX = newMin.getBlockX() - (int) min.x();
        int offsetY = newMin.getBlockY() - (int) min.y();
        int offsetZ = newMin.getBlockZ() - (int) min.z();

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

            newBlock.setData(blockState.getTypeData());
        }
    }

}

package us.smartmc.gamescore.instance.serialization;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.joml.Vector3d;
import us.smartmc.gamescore.instance.cuboid.Cuboid;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CuboidWrapperBuilder {

    private final CuboidWrapper wrapper;

    // Constructor que toma un cuboide y serializa los bloques dentro de la región
    public CuboidWrapperBuilder(Cuboid cuboid) {
        Vector3d min = new Vector3d(cuboid.getMin().getX(), cuboid.getMin().getY(), cuboid.getMin().getZ());
        Vector3d max = new Vector3d(cuboid.getMax().getX(), cuboid.getMax().getY(), cuboid.getMax().getZ());

        Location minLoc = cuboid.getMin();
        Location maxLoc = cuboid.getMax();

        List<BlockStateWrapper> blocks = new ArrayList<>();

        for (int x = minLoc.getBlockX(); x <= maxLoc.getBlockX(); x++) {
            for (int y = minLoc.getBlockY(); y <= maxLoc.getBlockY(); y++) {
                for (int z = minLoc.getBlockZ(); z <= maxLoc.getBlockZ(); z++) {
                    Block block = minLoc.getWorld().getBlockAt(x, y, z);
                    // Omitir los bloques de aire para no serializarlos
                    if (block.getType() != Material.AIR) {
                        blocks.add(new BlockStateWrapperBuilder(block).getWrapper());
                    }
                }
            }
        }
        this.wrapper = new CuboidWrapper(min, max, blocks);
    }

}

package us.smartmc.gamescore.instance.serialization;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.joml.Vector3i;
import us.smartmc.gamescore.instance.cuboid.Cuboid;
import us.smartmc.gamescore.instance.cuboid.CuboidBukkit;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CuboidWrapperBuilder {

    private final CuboidWrapper wrapper;

    // Constructor que toma un cuboide y serializa los bloques dentro de la región
    public CuboidWrapperBuilder(CuboidBukkit cuboid) {
        Vector3i min = new Vector3i(cuboid.getMin().x(), cuboid.getMin().y(), cuboid.getMin().z());
        Vector3i max = new Vector3i(cuboid.getMax().x(), cuboid.getMax().y(), cuboid.getMax().z());

        Location minLoc = cuboid.getMinLocation();
        Location maxLoc = cuboid.getMaxLocation();

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

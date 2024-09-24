package us.smartmc.gamescore.instance.serialization;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.io.Serializable;

@Getter
public class BlockStateWrapper implements Serializable {

    private final int x, y, z;

    private final String type;

    // Constructor para crear el wrapper a partir de un bloque.
    public BlockStateWrapper(Block block) {
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
        this.type = block.getType().name();
    }

    // Método para deserializar y aplicar el estado del bloque en el mundo.
    public void apply(World world) {
        Location location = new Location(world, x, y, z);
        Block block = location.getBlock();
        Material material;
        try {
            material = Material.valueOf(this.type);
        } catch (Exception e) {
            material = Material.BEDROCK;
        }
        block.setType(material);
    }

    @Override
    public String toString() {
        return "BlockStateWrapper{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", material=" + type +
                '}';
    }
}

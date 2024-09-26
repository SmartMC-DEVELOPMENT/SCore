package us.smartmc.gamescore.instance.serialization;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class BlockStateWrapper implements Serializable {

    private final int x, y, z;

    private final String type;
    private final byte typeData;

    // Constructor para crear el wrapper a partir de un bloque.
    public BlockStateWrapper(int x, int y, int z, String type, byte typeData) {
       this.x = x;
       this.y = y;
       this.z = z;
       this.type = type;
       this.typeData = typeData;
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

package us.smartmc.gamescore.instance.serialization;

import lombok.Getter;
import org.joml.Vector3d;

import java.io.Serializable;
import java.util.List;

@Getter
public class CuboidWrapper implements Serializable {

    private final List<BlockStateWrapper> blocks;
    private final Vector3d min, max;

    // Constructor que toma un cuboide y serializa los bloques dentro de la región
    public CuboidWrapper(Vector3d min, Vector3d max, List<BlockStateWrapper> blocks) {
        this.min = min;
        this.max = max;
        this.blocks = blocks;
    }
}

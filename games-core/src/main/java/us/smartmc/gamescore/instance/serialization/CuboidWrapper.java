package us.smartmc.gamescore.instance.serialization;

import lombok.Getter;
import org.joml.Vector3i;

import java.io.Serializable;
import java.util.List;

@Getter
public class CuboidWrapper implements Serializable {

    private final List<BlockStateWrapper> blocks;
    private final Vector3i min, max;

    // Constructor que toma un cuboide y serializa los bloques dentro de la región
    public CuboidWrapper(Vector3i min, Vector3i max, List<BlockStateWrapper> blocks) {
        this.min = min;
        this.max = max;
        this.blocks = blocks;
    }
}

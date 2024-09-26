package us.smartmc.gamescore.instance.serialization;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class BlockStateWrapperBuilder {

    @Getter
    private final BlockStateWrapper wrapper;

    public BlockStateWrapperBuilder(Block block) {
        Location location = block.getLocation();
        wrapper = new BlockStateWrapper(location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ(),
                block.getType().name(),
                block.getData());
    }

}

package us.smartmc.core.regions;

import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public abstract class RegionMetaEvent implements IRegionMetaEvent {


    private final RegionMetaEventType type;

    public RegionMetaEvent(RegionMetaEventType type) {
        this.type = type;
    }

    @Override
    public void onEvent(Player player, RegionConfig config) {

    }

    @Override
    public void onEvent(RegionConfig config) {

    }
}
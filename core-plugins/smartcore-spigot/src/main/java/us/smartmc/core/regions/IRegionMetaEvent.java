package us.smartmc.core.regions;

import org.bukkit.entity.Player;

public interface IRegionMetaEvent {

    void onEvent(Player player, RegionConfig config);
    void onEvent(RegionConfig config);

    RegionMetaEventType getType();
}
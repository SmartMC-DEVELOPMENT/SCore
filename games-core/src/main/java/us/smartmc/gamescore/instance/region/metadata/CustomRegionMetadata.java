package us.smartmc.gamescore.instance.region.metadata;

import lombok.Getter;
import org.bukkit.Location;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboidRegion;
import us.smartmc.gamescore.instance.cuboid.CuboidRegion;
import us.smartmc.gamescore.manager.RegionsManager;

import java.util.HashSet;
import java.util.Set;

@Getter
public class CustomRegionMetadata implements ICustomRegionMetadata {

    private static final RegionsManager manager = RegionsManager.getManager(RegionsManager.class);

    protected final String metadataId;

    public CustomRegionMetadata(String metadataId) {
        this.metadataId = metadataId;
    }

    @Override
    public boolean isActiveMetadata(Location location) {
        for (CuboidRegion region : getRegionByLocation(location)) {
            if (region.getDefaultConfig().hasMetaData(getMetadataId())) return true;
        }
        return false;
    }

    private Set<CuboidRegion> getRegionByLocation(Location location) {
        Set<CuboidRegion> list = new HashSet<CuboidRegion>();
        for (CuboidRegion region : manager.values()) {
            if (region instanceof BukkitCuboidRegion bukkitCuboidRegion && bukkitCuboidRegion.getCuboid().contains(location))
                list.add(region);
        }
        return list;
    }

}

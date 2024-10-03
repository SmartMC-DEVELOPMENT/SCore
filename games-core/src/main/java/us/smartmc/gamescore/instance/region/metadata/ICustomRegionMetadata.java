package us.smartmc.gamescore.instance.region.metadata;

import org.bukkit.Location;
import org.bukkit.event.Listener;

public interface ICustomRegionMetadata extends Listener {

    boolean isActiveMetadata(Location location);
    String getMetadataId();

}

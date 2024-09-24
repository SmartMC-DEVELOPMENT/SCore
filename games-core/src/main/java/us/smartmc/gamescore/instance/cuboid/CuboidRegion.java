package us.smartmc.gamescore.instance.cuboid;

import lombok.Getter;
import me.imsergioh.pluginsapi.util.LocationSerializer;
import org.bukkit.Location;

@Getter
public class CuboidRegion {

    private final String name;
    private final Cuboid cuboid;

    private final CuboidRegionConfig config;

    // Create new cuboid region & config
    public CuboidRegion(String name, Cuboid cuboid) {
        this.name = name;
        this.cuboid = cuboid;
        this.config = new CuboidRegionConfig(name);
        setCuboidLocations(config);
        config.save();
    }

    // Load
    public CuboidRegion(String name) {
        this.name = name;
        this.config = new CuboidRegionConfig(name);
        config.load();
        this.cuboid = new Cuboid(getMinLocation(config), getMaxLocation(config));
    }

    private void setCuboidLocations(CuboidRegionConfig config) {
        config.set("min", LocationSerializer.toString(cuboid.getMin()));
        config.set("max", LocationSerializer.toString(cuboid.getMax()));
    }

    private static Location getMaxLocation(CuboidRegionConfig config) {
        return LocationSerializer.toLocation(config.getString("max"));
    }

    private static Location getMinLocation(CuboidRegionConfig config) {
        return LocationSerializer.toLocation(config.getString("min"));
    }

}

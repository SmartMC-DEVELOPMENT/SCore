package us.smartmc.gamescore.instance.cuboid;

import lombok.Getter;
import me.imsergioh.pluginsapi.util.LocationSerializer;
import org.bukkit.Location;

@Getter
public class CuboidRegion {

    private final String name;
    private final CuboidBukkit cuboid;

    private final CuboidRegionConfig config;

    // Create new cuboid region & config
    public CuboidRegion(String name, CuboidBukkit cuboid) {
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
        this.cuboid = new CuboidBukkit(getMinLocation(config), getMaxLocation(config));
    }

    // Subregion
    public CuboidRegion(CuboidRegion parent, String name) {
        this.name = name;
        this.config = new CuboidRegionConfig(name);
        config.load();
        this.cuboid = new CuboidBukkit(getMinLocation(config), getMaxLocation(config));
    }

    public CuboidRegion getSubRegion(String name) {
        return config.getSubRegions().get(name);
    }

    private void setCuboidLocations(CuboidRegionConfig config) {
        config.set("min", LocationSerializer.toString(cuboid.getMinLocation()));
        config.set("max", LocationSerializer.toString(cuboid.getMaxLocation()));
    }

    private static Location getMaxLocation(CuboidRegionConfig config) {
        return LocationSerializer.toLocation(config.getString("max"));
    }

    private static Location getMinLocation(CuboidRegionConfig config) {
        return LocationSerializer.toLocation(config.getString("min"));
    }

}

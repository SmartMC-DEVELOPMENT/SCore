package us.smartmc.gamescore.instance.cuboid;

import lombok.Getter;
import org.joml.Vector3i;
import us.smartmc.gamescore.util.CuboidUtil;

@Getter
public class CuboidRegion {

    protected final String name;
    protected final Cuboid cuboid;

    protected final CuboidRegionConfig config;

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
        this.cuboid = new Cuboid(getMinVector(config), getMaxVector(config));
    }

    // Subregion
    public CuboidRegion(CuboidRegion parent, String name) {
        this.name = name;
        this.config = new CuboidRegionConfig(name);
        config.load();
        this.cuboid = new Cuboid(getMinVector(config), getMaxVector(config));
    }

    public CuboidRegion getSubRegion(String name) {
        return config.getSubRegions().get(name);
    }

    private void setCuboidLocations(CuboidRegionConfig config) {
        config.set("min", CuboidUtil.vectorToString(cuboid.getMin()));
        config.set("max", CuboidUtil.vectorToString(cuboid.getMax()));
    }

    private static Vector3i getMaxVector(CuboidRegionConfig config) {
        return CuboidUtil.stringToVector(config.getString("max"));
    }

    private static Vector3i getMinVector(CuboidRegionConfig config) {
        return CuboidUtil.stringToVector(config.getString("min"));
    }
}

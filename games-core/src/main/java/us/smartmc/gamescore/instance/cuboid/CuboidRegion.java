package us.smartmc.gamescore.instance.cuboid;

import lombok.Getter;
import org.joml.Vector3i;
import us.smartmc.gamescore.util.CuboidUtil;

@Getter
public class CuboidRegion {

    protected final String name;
    protected final Cuboid defaultCuboid;

    private final CuboidRegionConfig defaultConfig;

    // Create new cuboid region & config
    public CuboidRegion(String name, Cuboid cuboid) {
        this.name = name;
        this.defaultCuboid = cuboid;
        this.defaultConfig = new CuboidRegionConfig(name);
        setCuboidLocations(defaultConfig);
        defaultConfig.save();
    }

    // Load
    public CuboidRegion(String name) {
        this.name = name;
        this.defaultConfig = new CuboidRegionConfig(name);
        defaultConfig.load();
        this.defaultCuboid = new Cuboid(getMinVector(defaultConfig), getMaxVector(defaultConfig));
    }

    // Subregion
    public CuboidRegion(CuboidRegion parent, String name) {
        this.name = name;
        this.defaultConfig = new CuboidRegionConfig(name);
        defaultConfig.load();
        this.defaultCuboid = new Cuboid(getMinVector(defaultConfig), getMaxVector(defaultConfig));
    }

    public CuboidRegion getSubRegion(String name) {
        return defaultConfig.getSubRegions().get(name);
    }

    private void setCuboidLocations(CuboidRegionConfig config) {
        config.set("min", CuboidUtil.vectorToString(defaultCuboid.getMin()));
        config.set("max", CuboidUtil.vectorToString(defaultCuboid.getMax()));
    }

    private static Vector3i getMaxVector(CuboidRegionConfig config) {
        return CuboidUtil.stringToVector(config.getString("max"));
    }

    private static Vector3i getMinVector(CuboidRegionConfig config) {
        return CuboidUtil.stringToVector(config.getString("min"));
    }
}

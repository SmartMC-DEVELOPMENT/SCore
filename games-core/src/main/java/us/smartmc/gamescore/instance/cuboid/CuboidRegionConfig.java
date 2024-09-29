package us.smartmc.gamescore.instance.cuboid;

import lombok.Getter;
import us.smartmc.gamescore.instance.storage.YamlData;
import us.smartmc.gamescore.manager.RegionsManager;

import java.io.File;
import java.util.*;

@Getter
public class CuboidRegionConfig extends YamlData {

    protected static final String SUBREGIONS_KEY = "subregions";

    private final String name;

    private final Set<String> metaData = new HashSet<>();
    private final Map<String, CuboidRegion> subRegions = new HashMap<>();

    // Default region config (no subregion)
    public CuboidRegionConfig(String name) {
        super(getFileRegion(name));
        loadRegionData();
        this.name = name;
    }

    // Subregion into a existent CuboidRegion (subregion)
    public CuboidRegionConfig(String name, CuboidRegionConfig parentConfig) {
        super(getFileRegion(parentConfig.getName()));
        loadRegionData(parentConfig.getName());
        this.name = name;
    }

    public void unregisterSubRegion(CuboidRegion region) {
        String path = SUBREGIONS_KEY + "." + region.getName();
        set(path, null);
        save();
        subRegions.remove(region.getName());
    }

    public void registerSubRegion(CuboidRegion region) {
        String path = SUBREGIONS_KEY + "." + region.getName();
        CuboidRegionConfig subRegionConfig = new CuboidRegionConfig(region.getName(), this);
        set(path, subRegionConfig.getData());
        save();
        subRegions.put(region.getName(), region);
    }

    private static File getFileRegion(String name) {
        return new File(RegionsManager.getRegionsDirectory(), name + ".yml");
    }

    public void addMetaData(String value) {
        metaData.add(value);
    }

    public void removeMetaData(String value) {
        metaData.remove(value);
    }

    public void loadRegionData(String key) {
        String path = key == null ? "" : SUBREGIONS_KEY + "." + key;

        if (containsKey(getKey(path, "metadata"))) {
            metaData.addAll(getList("metadata", String.class));
        }
    }

    public void loadRegionData() {
        loadRegionData(null);
    }

    @Override
    public void save() {
        if (!metaData.isEmpty()) {
            set("metadata", new ArrayList<>(metaData));
        }
        super.save();
    }

    protected static String getKey(String path, String name) {
        return path.isEmpty() ? name : path + "." + name;
    }

    private static RegionsManager getManager() {
        return RegionsManager.getManager(RegionsManager.class);
    }
}

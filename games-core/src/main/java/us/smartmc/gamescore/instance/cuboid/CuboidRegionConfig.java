package us.smartmc.gamescore.instance.cuboid;

import lombok.Getter;
import us.smartmc.gamescore.instance.storage.YamlData;
import us.smartmc.gamescore.manager.RegionsManager;

import java.io.File;
import java.util.*;

@Getter
public class CuboidRegionConfig extends YamlData {

    private final String name;

    private final Set<String> metaData = new HashSet<>();

    public CuboidRegionConfig(String name) {
        super(getFileRegion(name));
        this.name = name;
        load();
        loadRegionData();
    }

    private static File getFileRegion(String name) {
        return new File(RegionsManager.getRegionsDirectory(), name + ".yml");
    }

    public void addMetaData(String value) {
        metaData.add(value);
    }

    public boolean hasMetaData(String value) {
        return metaData.contains(value);
    }

    public boolean toggleMetadata(String value) {
        if (metaData.contains(value)) {
            removeMetaData(value);
            return false;
        }
        addMetaData(value);
        return true;
    }

    public Set<String> getMetadata() {
        return new HashSet<>(getList("metadata", String.class));
    }

    public void removeMetaData(String value) {
        metaData.remove(value);
    }

    public void loadRegionData() {
        if (containsKey("metadata")) {
            metaData.addAll(getMetadata());
        }
    }

    public void saveMetadata() {
        set("metadata", new ArrayList<>(metaData));
        save();
    }
}

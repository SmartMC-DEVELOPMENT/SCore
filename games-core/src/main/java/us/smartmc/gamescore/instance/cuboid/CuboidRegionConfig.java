package us.smartmc.gamescore.instance.cuboid;

import lombok.Getter;
import us.smartmc.gamescore.instance.storage.YamlData;
import us.smartmc.gamescore.manager.RegionsManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Getter
public class CuboidRegionConfig extends YamlData {

    private final Set<String> metaData = new HashSet<>();

    public CuboidRegionConfig(String regionName) {
        super(getFileRegion(regionName));
    }

    private static File getFileRegion(String name) {
        return new File(RegionsManager.getRegionsDirectory(), name + ".yml");
    }

    @Override
    public void load() {
        super.load();
        if (containsKey("metadata")) {
            metaData.addAll(getList("metadata", String.class));
        }
    }

    @Override
    public void save() {
        if (!metaData.isEmpty()) {
            set("metadata", new ArrayList<>(metaData));
        }
        super.save();
    }
}

package us.smartmc.gamescore.instance.cuboid;

import us.smartmc.gamescore.instance.storage.YamlData;
import us.smartmc.gamescore.manager.RegionsManager;

import java.io.File;

public class CuboidRegionConfig extends YamlData {

    public CuboidRegionConfig(String regionName) {
        super(getFileRegion(regionName));
    }

    private static File getFileRegion(String name) {
        return new File(RegionsManager.getRegionsDirectory(), name + ".yml");
    }



}

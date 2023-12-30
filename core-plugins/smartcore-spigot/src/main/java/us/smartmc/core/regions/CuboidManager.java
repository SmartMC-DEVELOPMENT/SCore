package us.smartmc.core.regions;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.manager.ManagerRegistry;
import org.bukkit.Location;
import us.smartmc.core.SmartCore;

import java.io.File;
import java.util.*;

public class CuboidManager extends ManagerRegistry<String, RegionConfig> {

    private static final SmartCore plugin = SmartCore.getPlugin();

    @Getter
    private static final File regionsParentDirectory = new File(plugin.getDataFolder() + "/regions");

    @Override
    public void load() {
        regionsParentDirectory.mkdirs();
        for (File file : Objects.requireNonNull(regionsParentDirectory.listFiles())) {
            if (!file.getName().endsWith(".yml")) continue;
            String name = file.getName().replace(".yml", "");
            register(name, new RegionConfig(name));
        }
    }

    @Override
    public void unload() {
        for (RegionConfig config : values()) {
            config.save();
        }
    }

    public Map<String, Cuboid> getCuboidsAtLocation(Location location) {
        Map<String, Cuboid> map = new HashMap<>();
        values().stream().filter(regionConfig -> regionConfig.getCuboidRegion().contains(location)).forEach(
                regionConfig -> map.put(regionConfig.getName(), regionConfig.getCuboidRegion()));
        return map;
    }

    public Set<Cuboid> getCuboids() {
        Set<Cuboid> list = new HashSet<>();
        values().forEach(config -> list.add(config.getCuboidRegion()));
        return list;
    }

}
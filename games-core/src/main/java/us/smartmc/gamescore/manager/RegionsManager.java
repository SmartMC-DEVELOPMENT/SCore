package us.smartmc.gamescore.manager;

import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.cuboid.Cuboid;
import us.smartmc.gamescore.instance.cuboid.CuboidRegion;
import us.smartmc.gamescore.instance.manager.MapManager;

import java.io.File;
import java.util.Objects;
import java.util.Optional;

public class RegionsManager extends MapManager<String, CuboidRegion> {

    private RegionsManager() {
        getRegionsDirectory().mkdirs();
        for (File file : Objects.requireNonNull(getRegionsDirectory().listFiles())) {
            if (!file.getName().endsWith(".yml")) continue;
            String name = file.getName().replace(".yml", "");
            loadRegion(name);
        }
    }

    public Optional<CuboidRegion> getRegion(String name) {
        if (!containsKey(name)) return Optional.empty();
        return Optional.of(get(name));
    }

    public CuboidRegion createRegion(String name, Cuboid cuboid) {
        return put(name, new CuboidRegion(name, cuboid));
    }

    public CuboidRegion loadRegion(String name) {
        return put(name, new CuboidRegion(name));
    }

    @Override
    public CuboidRegion createValueByKey(String key) {
        return new CuboidRegion(key, null);
    }

    public static File getRegionsDirectory() {
        return new File(GamesCoreAPI.getApi().getPlugin().getDataFolder() + "/../../regions");
    }

}

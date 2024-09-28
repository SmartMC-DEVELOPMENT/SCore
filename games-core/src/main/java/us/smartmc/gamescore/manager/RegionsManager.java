package us.smartmc.gamescore.manager;

import us.smartmc.gamescore.api.GamesCoreAPI;

import us.smartmc.gamescore.instance.cuboid.CuboidBukkit;
import us.smartmc.gamescore.instance.cuboid.CuboidRegion;
import us.smartmc.gamescore.instance.manager.MapManager;

import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class RegionsManager extends MapManager<String, CuboidRegion> {

    private RegionsManager() {
        getRegionsDirectory().mkdirs();
        for (File file : Objects.requireNonNull(getRegionsDirectory().listFiles())) {
            if (!file.getName().endsWith(".yml")) continue;
            String name = file.getName().replace(".yml", "");
            loadRegion(name);
        }
    }

    @Override
    public CuboidRegion get(Object key) {
        if (key instanceof String path && path.contains(".")) {
            final String[] pathParts = path.split("\\.");
            if (pathParts.length == 0) {
                throw new IllegalArgumentException("Invalid path: " + path);
            }

            final CuboidRegion parentRegion = get(pathParts[0]);

            final String[] remainingPathParts = new String[pathParts.length - 1];
            System.arraycopy(pathParts, 1, remainingPathParts, 0, remainingPathParts.length);

            return Stream.of(remainingPathParts).reduce(parentRegion,
                    (subRegion, part) -> subRegion.getSubRegion(part), (a, b) -> a);
        }
        return super.get(key);
    }

    public Optional<CuboidRegion> getRegion(String name) {
        if (!containsKey(name)) return Optional.empty();
        return Optional.of(get(name));
    }

    public CuboidRegion createRegion(String name, CuboidBukkit cuboid) {
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
        return new File(GamesCoreAPI.getApiOptional()
                .map(api -> api.getPlugin().getDataFolder() + "/../../regions")
                .orElse("D:\\workspace\\SCore\\games-core\\test_data"));
    }

}

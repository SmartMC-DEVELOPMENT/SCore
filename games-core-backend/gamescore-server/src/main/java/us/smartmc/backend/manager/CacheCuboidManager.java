package us.smartmc.backend.manager;

import us.smartmc.gamescore.instance.serialization.CuboidWrapper;

import java.util.HashMap;
import java.util.Map;

public class CacheCuboidManager {

    private static final Map<String, CuboidWrapper> cache = new HashMap<>();

    public static void saveCache(String name, CuboidWrapper wrapper) {
        cache.put(name, wrapper);
    }

    public static CuboidWrapper get(String name) {
        return cache.get(name);
    }

}

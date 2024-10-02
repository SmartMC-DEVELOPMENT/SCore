package us.smartmc.backend.manager;

import us.smartmc.gamescore.instance.serialization.CuboidWrapper;

import java.util.HashMap;
import java.util.Map;

public class TestCacheCuboidManager {

    private static final Map<String, CuboidWrapper> cache = new HashMap<>();

    public static void saveCache(String name, CuboidWrapper wrapper) {
        cache.put(name, wrapper);
        System.out.println("SAVING CACHE CUBOIDWRAPPER " + name);
    }

    public static CuboidWrapper get(String name) {
        System.out.println("GET CACHE CUBOIDWRAPPER " + name);
        return cache.get(name);
    }

}

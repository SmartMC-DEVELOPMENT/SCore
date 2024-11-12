package us.smartmc.core.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerVersions {

    private static final Map<UUID, Integer> registry = new HashMap<>();

    public static void unregister(UUID uuid) {
        registry.remove(uuid);
    }

    public static int get(UUID uuid) {
        return -1;
    }
}

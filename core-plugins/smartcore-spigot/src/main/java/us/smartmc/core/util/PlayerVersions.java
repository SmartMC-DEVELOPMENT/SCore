package us.smartmc.core.util;

import me.imsergioh.pluginsapi.connection.RedisConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerVersions {

    private static final Map<UUID, Integer> registry = new HashMap<>();

    public static void unregister(UUID uuid) {
        registry.remove(uuid);
    }

    public static int get(UUID uuid) {
        if (!registry.containsKey(uuid)) {
            String value = RedisConnection.mainConnection.getResource().get("playerVersion." + uuid);
            if (value == null) return -1;
            int version = Integer.parseInt(value);
            registry.put(uuid, version);
        }
        return registry.get(uuid);
    }

}

package us.smartmc.backend.connection.manager;

import us.smartmc.backend.instance.player.PlayerCache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCacheManager {

    private static long lastRequest = 0;
    private static final Map<UUID, PlayerCache> playerCaches = new HashMap<>();

    public static void registerRequestDate() {
        lastRequest = System.currentTimeMillis();
    }

    public static void parse() {
        long millis = System.currentTimeMillis();
        long start = lastRequest;
        long retardo = (millis - start);

        System.out.println("RETARDO DE " + retardo + "ms");
    }

    public static PlayerCache get(UUID uuid) {
        return playerCaches.get(uuid);
    }
}

package us.smartmc.backend.connection.manager;

import us.smartmc.backend.instance.player.PlayerCache;

import java.io.FilterInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCacheManager {

    private static final Map<UUID, Long> startDate = new HashMap<>();
    FilterInputStream in = null;
    private static final Map<UUID, PlayerCache> playerCaches = new HashMap<>();

    public static void registerStartDate(UUID uuid) {
        startDate.put(uuid, System.currentTimeMillis());
    }

    public static void parse(PlayerCache cache) {
        long millis = System.currentTimeMillis();
        long start = startDate.get(cache.getPlayerId());
        System.out.println("GETTED PLAYER CACHE!" + cache.getCacheMap());
        playerCaches.put(cache.getPlayerId(), cache);
        System.out.println("RETARDO DE " + (millis - start) / 1000.0 + "s");
    }

    public static PlayerCache get(UUID uuid) {
        return playerCaches.get(uuid);
    }
}

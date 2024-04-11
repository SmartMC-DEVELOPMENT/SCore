package us.smartmc.backend.handler;

import us.smartmc.backend.instance.PlayerCache;
import us.smartmc.backend.instance.PlayerContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerContextsHandler {

    private static final Map<UUID, PlayerContext> contexts = new HashMap<>();

    public static PlayerContext getOrCreate(UUID playerId) {
        PlayerContext context = contexts.get(playerId);
        if (context != null) return context;
        context = new PlayerContext(playerId);
        contexts.put(playerId, context);
        return context;
    }

    public static void remove(UUID playerId) {
        System.out.println(getOrCreate(playerId).getCache().getCacheMap());
        contexts.remove(playerId);
        System.out.println("Removed playercontext " + playerId);
    }

    public static void setCache(UUID id, String key, Object value) {
        getOrCreate(id).getCache().set(key, value);
        System.out.println("Cache value " + key + " ? " + value + " has been set! " + id);
    }

    public static Object getCacheValue(UUID id, String key) {
        return getOrCreate(id).getCache().get(key);
    }

    public static PlayerCache getCache(UUID id) {
        return getOrCreate(id).getCache();
    }
}

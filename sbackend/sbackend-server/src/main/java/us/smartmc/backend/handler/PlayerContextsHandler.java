package us.smartmc.backend.handler;

import us.smartmc.backend.instance.handler.MapHandler;
import us.smartmc.backend.instance.player.PlayerCache;
import us.smartmc.backend.instance.player.PlayerContext;
import us.smartmc.backend.util.ConsoleUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerContextsHandler extends MapHandler<UUID, PlayerContext> {

    private static final Map<UUID, PlayerCache> playerCaches = new HashMap<>();

    @Override
    public void remove(UUID playerId) {
        super.remove(playerId);
        ConsoleUtil.print("Removed playercontext " + playerId);
    }

    public void setCache(UUID id, String key, Object value) {
        getOrCreate(id).getCache().set(key, value);
        System.out.println("Cache value " + key + " ? " + value + " has been set! " + id);
    }

    public Object getCacheValue(UUID id, String key) {
        return getOrCreate(id).getCache().get(key);
    }

    public PlayerCache getCache(UUID id) {
        if (playerCaches.containsKey(id)) {
            return playerCaches.get(id);
        }
        PlayerCache cache = getOrCreate(id).getCache();
        playerCaches.put(id, cache);
        return cache;
    }

    @Override
    public PlayerContext getDefaultValue(UUID playerId) {
        return new PlayerContext(playerId);
    }
}

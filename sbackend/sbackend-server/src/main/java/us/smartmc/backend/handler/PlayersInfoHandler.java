package us.smartmc.backend.handler;

import us.smartmc.backend.instance.handler.MapHandler;
import us.smartmc.backend.instance.player.PlayerCache;
import us.smartmc.backend.util.ConsoleUtil;

import java.util.UUID;

public class PlayersInfoHandler extends MapHandler<UUID, PlayerCache> {

    @Override
    public void remove(UUID playerId) {
        super.remove(playerId);
        ConsoleUtil.print("Removed playercontext " + playerId);
    }

    public void setCache(UUID id, String key, Object value) {
        getOrCreate(id).set(key, value);
        System.out.println("Cache value " + key + " ? " + value + " has been set! " + id);
    }

    public Object getCacheValue(UUID id, String key) {
        return getOrCreate(id).get(key);
    }

    public PlayerCache getCache(UUID id) {
        return getOrCreate(id);
    }

    @Override
    public PlayerCache getDefaultValue(UUID playerId, Object... args) {
        return new PlayerCache(playerId);
    }
}

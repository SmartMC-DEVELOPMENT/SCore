package us.smartmc.backend.handler;

import us.smartmc.backend.instance.handler.MapHandler;
import us.smartmc.backend.instance.player.PlayerCache;
import us.smartmc.backend.util.ConsoleUtil;

import java.util.Random;
import java.util.UUID;

public class PlayersInfoHandler extends MapHandler<String, PlayerCache> {

    @Override
    public void remove(String id) {
        super.remove(id);
        ConsoleUtil.print("Removed playercontext " + id);
    }

    public void setCache(UUID id, String key, Object value) {
        getOrCreate("testCache." + id).set(key, value);
        System.out.println("Cache value " + key + " ? " + value + " has been set! " + id);
    }

    public Object getCacheValue(UUID id, String key) {
        return getOrCreate("testCache." + id).get(key);
    }

    public PlayerCache getCache(String id) {
        return getOrCreate(id);
    }

    @Override
    public PlayerCache getDefaultValue(String id, Object... args) {
        return new PlayerCache(id);
    }
}

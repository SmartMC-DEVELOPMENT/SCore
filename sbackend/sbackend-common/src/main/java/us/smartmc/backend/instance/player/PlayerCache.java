package us.smartmc.backend.instance.player;

import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class PlayerCache implements Serializable {

    private final UUID playerId;
    private final Map<Object, Object> cacheMap = new HashMap<>();

    public PlayerCache(String id) {
        this.playerId = UUID.fromString(id.split("\\.")[1]);
        cacheMap.put("_id", playerId.toString());
    }

    public void remove(Object key) {
        cacheMap.remove(key);
    }

    public void set(Object key, Object value) {
        cacheMap.put(key, value);
    }

    public int getInt(Object key) {
        return (int) get(key);
    }

    public String getString(Object key) {
        return (String) get(key);
    }

    public Object get(Object key) {
        return cacheMap.get(key);
    }

}

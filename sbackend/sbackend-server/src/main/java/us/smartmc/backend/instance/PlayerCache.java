package us.smartmc.backend.instance;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class PlayerCache {

    @Getter
    private final Map<String, Object> cacheMap = new HashMap<>();

    public void set(String key, Object value) {
        cacheMap.put(key, value);
    }

    public int getInt(String key) {
        return (int) get(key);
    }

    public String getString(String key) {
        return (String) get(key);
    }

    public Object get(String key) {
        return cacheMap.get(key);
    }

}

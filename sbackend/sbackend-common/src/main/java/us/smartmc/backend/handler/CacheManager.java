package us.smartmc.backend.handler;

import lombok.Getter;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class CacheManager extends Document {

    private static final Map<String, CacheManager> managers = new HashMap<>();

    @Getter
    private static CacheManager mainCacheManager = new CacheManager("main");

    @Getter
    private final String id;

    public CacheManager(String id) {
        this.id = id;
        managers.put(id, this);
    }

    public boolean register(String key, Object value) {
        if (containsKey(key)) return false;
        put(key, value);
        return true;
    }

    public static CacheManager getManagerById(String id) {
        return managers.get(id);
    }

}

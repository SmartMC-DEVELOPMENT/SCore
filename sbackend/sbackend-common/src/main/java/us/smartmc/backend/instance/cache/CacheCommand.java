package us.smartmc.backend.instance.cache;

import lombok.Getter;
import lombok.Setter;
import us.smartmc.backend.handler.CacheManager;

@Getter
public class CacheCommand {

    @Setter
    private String id = "main";
    private final CacheCommandType type;
    private final String key;

    @Setter
    private Object value;

    private CacheCommand(CacheCommandType type, String key) {
        this.type = type;
        this.key = key;
    }

    private CacheCommand(CacheCommandType type, String key, Object value) {
        this("main", type, key, value);
    }

    private CacheCommand(String id, CacheCommandType type, String key) {
        this(type, key);
        this.id = id;
    }

    private CacheCommand(String id, CacheCommandType type, String key, Object value) {
        this(id, type, key);
        this.value = value;
    }

    public CacheCommand value(Object value) {
        this.value = value;
        return this;
    }

    public CacheManager getCacheManager() {
        CacheManager cacheManager = CacheManager.getManagerById(id);
        return cacheManager != null ? cacheManager : CacheManager.getMainCacheManager();
    }

    public static CacheCommand build(CacheCommandType type, String key) {
        return new CacheCommand("main", type, key);
    }

    public static CacheCommand build(CacheCommandType type, String id, String key) {
        return new CacheCommand(id, type, key);
    }
}

package us.smartmc.backend.instance.cache;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CacheFile {

    private static final Map<String, CacheFile> cacheFiles = new HashMap<>();

    private final String name;
    private final String content;

    public CacheFile(String name, String content) {
        this.name = name;
        this.content = content;
        cacheFiles.put(name, this);
    }

    public static CacheFile get(String name) {
        return cacheFiles.get(name);
    }
}

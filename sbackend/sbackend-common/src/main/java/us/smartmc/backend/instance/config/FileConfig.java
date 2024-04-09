package us.smartmc.backend.instance.config;

import lombok.Getter;

import java.io.File;
import java.util.HashMap;

public abstract class FileConfig implements IConfig {

    protected HashMap data = new HashMap<>();

    @Getter
    private final File file;

    public FileConfig(File file) {
        this.file = file;
    }

    @Override
    public void registerDefaultValue(String path, Object value) {
        if (!data.containsKey(path)) return;
        data.put(path, value);
    }

    @Override
    public void set(String path, Object value) {
        data.put(path, value);
    }

    @Override
    public Object get(String path) {
        return data.get(path);
    }
}

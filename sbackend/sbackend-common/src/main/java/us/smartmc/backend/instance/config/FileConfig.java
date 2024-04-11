package us.smartmc.backend.instance.config;

import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public abstract class FileConfig implements IConfig {

    protected HashMap<String, Object> data = new HashMap<>();

    @Getter
    protected final File file;

    public FileConfig(File file) {
        this.file = file;
        file.getParentFile().mkdirs();
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void registerDefaultValue(String path, Object value) {
        if (!data.containsKey(path)) data.put(path, value);
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

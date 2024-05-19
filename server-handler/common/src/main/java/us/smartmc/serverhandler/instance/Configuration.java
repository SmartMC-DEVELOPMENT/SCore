package us.smartmc.serverhandler.instance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import us.smartmc.serverhandler.manager.ConfigManager;

import java.io.*;
import java.lang.reflect.Type;

public abstract class Configuration<T extends Serializable> implements IConfiguration {

    @Getter
    private final String name;
    private final File file;

    @Getter
    private T data;

    public Configuration(String parentDir, String name) {
        this.name = name;
        file = new File(parentDir, name + ".json");
        loadData();
        ConfigManager.register(this);
    }

    private void loadData() {
        Gson gson = new GsonBuilder().create();
        try (Reader reader = new FileReader(file.getAbsolutePath())) {
            // Load from file and set
            Type dataType = getDefaultType();
            data = gson.fromJson(reader, dataType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Configuration<?> get(String name) {
        return ConfigManager.get(name);
    }

}

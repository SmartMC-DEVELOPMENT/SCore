package us.smartmc.serverhandler.instance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import us.smartmc.serverhandler.OrchestratorMain;
import us.smartmc.serverhandler.manager.ConfigManager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Configuration {

    private static final String parentDir = "/home/network/server-handler/config";

    private final String name;
    private final File file;

    private ConfigurationData data;

    public Configuration(String name) {
        this.name = name;
        file = new File(parentDir, name + ".json");
        loadData();
        ConfigManager.register(this);
    }

    private void loadData() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Reader reader = new FileReader(file.getAbsolutePath())) {
            // Load from file and set
            data = gson.fromJson(reader, ConfigurationData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigurationData getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public static File getParent() {
        return new File(parentDir);
    }

    public static Configuration get(String name) {
        return ConfigManager.get(name);
    }

}

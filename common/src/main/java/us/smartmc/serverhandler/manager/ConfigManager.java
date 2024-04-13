package us.smartmc.serverhandler.manager;

import us.smartmc.serverhandler.instance.Configuration;

import java.util.HashMap;

public class ConfigManager {

    private static final HashMap<String, Configuration<?>> configs = new HashMap<>();

    public static void register(Configuration<?> configuration) {
        configs.put(configuration.getName(), configuration);
    }

    public static Configuration<?> get(String name) {
        return configs.get(name);
    }

    public static Configuration<?> getByPrefixName(String serverName) {
        for (String configName : configs.keySet()) {
            if (serverName.startsWith(configName)) {
                return configs.get(configName);
            }
        }
        return null;
    }

}

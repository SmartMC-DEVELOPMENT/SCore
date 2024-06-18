package us.smartmc.smartcore.smartcorevelocity.manager;

import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import us.smartmc.smartcore.smartcorevelocity.SmartCoreVelocity;

import java.io.File;
import java.util.*;

public class HostnameRulesManager {

    private static final SmartCoreVelocity plugin = SmartCoreVelocity.getPlugin();

    private static final Map<String, FilePluginConfig> configs = new HashMap<>();

    public static void load() {
        File dir = new File(plugin.getDataDirectory() + "/hostnameRules");
        dir.mkdirs();
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            String name = file.getName();
            if (!name.endsWith(".json")) continue;
            name = name.replace(".json", "");
            FilePluginConfig config = new FilePluginConfig(file).load();
            configs.put(name, config);
        }
    }

    public static List<String> getHostnameRedirections(String key) {
        FilePluginConfig config = get(key);
        if (config == null) return List.of();
        return config.getList("servers", String.class, new ArrayList<>());
    }

    public static List<String> getBossbarTexts(String key) {
        FilePluginConfig config = get(key);
        if (config == null) return List.of();
        return config.getList("bossbars", String.class, new ArrayList<>());
    }

    public static FilePluginConfig get(String key) {
        return configs.get(key);
    }

}

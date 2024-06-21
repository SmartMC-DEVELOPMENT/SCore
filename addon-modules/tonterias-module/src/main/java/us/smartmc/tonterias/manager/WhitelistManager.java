package us.smartmc.tonterias.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import us.smartmc.tonterias.TonteriasModule;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class WhitelistManager {

    private static final TonteriasModule module = TonteriasModule.getModule();

    private static File file;
    private static FileConfiguration config;

    public static List<String> getWhitelist() {
        List<String> list = config.getStringList("list");
        list.replaceAll(String::toLowerCase);
        return list;
    }

    public static void load() {
        file = new File(module.getDataFolder(), "whitelist.yml");
        config = YamlConfiguration.loadConfiguration(file);
        if (!config.contains("list")) {
            config.set("list", List.of("ImSergioh", "SergiohDev", "ItzDavidesV2"));
        }
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

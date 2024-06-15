package us.smartmc.game.manager;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import us.smartmc.game.SkyBlockPlugin;
import us.smartmc.skyblock.instance.SkyBlockServerType;

import java.io.File;

public class ConfigsManager {

    private static final SkyBlockPlugin plugin = SkyBlockPlugin.getPlugin();

    @Getter
    private static SpigotYmlConfig config;

    public static void load() {
        config = new SpigotYmlConfig(getConfigFile("config.yml"));
        config.register("serverType", SkyBlockServerType.SPAWN);
    }

    private static File getConfigFile(String path) {
        return new File(plugin.getDataFolder() + " /" + path);
    }

}

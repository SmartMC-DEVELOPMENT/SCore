package us.smartmc.core.randomwar.config;

import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import us.smartmc.core.randomwar.RandomWar;

public class MainPluginConfig extends FilePluginConfig {

    private static final RandomWar plugin = RandomWar.getPlugin();

    public MainPluginConfig() {
        super(plugin.getDataFolder() + "/config.json");
        registerDefault("");
    }
}

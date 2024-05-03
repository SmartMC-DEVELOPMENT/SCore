package us.smartmc.core.randomwar.instance.game;

import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import us.smartmc.core.randomwar.RandomBattle;

import java.io.File;

public class GameTemplate {

    private static final RandomBattle plugin = RandomBattle.getPlugin();
    private static final File TEMPLATES_DIR = new File(plugin.getDataFolder() + "/templates");

    private final String name;
    private final FilePluginConfig config;

    private int maxTeamSize;
    private int maxTeamsAmount;

    public GameTemplate(String name) {
        this.name = name;
        this.config = getConfig(name).load();
        registerConfigDefaults();
    }

    private void registerConfigDefaults() {
        maxTeamSize = config.getInteger("maxTeamsSize");
        maxTeamsAmount = config.getInteger("maxTeamsAmount");
    }

    private static FilePluginConfig getConfig(String name) {
        return new FilePluginConfig(new File(TEMPLATES_DIR, name + ".json"));
    }

}

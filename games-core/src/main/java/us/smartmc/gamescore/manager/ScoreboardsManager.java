package us.smartmc.gamescore.manager;

import me.imsergioh.pluginsapi.language.Language;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.PluginScoreboard;
import us.smartmc.gamescore.instance.manager.MapManager;

import java.io.File;

public class ScoreboardsManager extends MapManager<String, PluginScoreboard> {

    public ScoreboardsManager() {
        // Load scoreboards from folder (register & load)
        getScoreboardsFolder().mkdirs();
        for (File file : getScoreboardsFolder().listFiles()) {
            String name = file.getName();
            if (!name.endsWith(".yml")) continue;
            String fileName = name.replace(".yml", "");
            register(fileName).load();
        }
    }

    public static File getScoreboardsFolder() {
        return new File(GamesCoreAPI.getApi().getPlugin().getDataFolder() + "//scoreboards");
    }

    @Override
    public PluginScoreboard createValueByKey(String name) {
        String[] args = name.split("_");
        String genericName = args[0];
        String langName = args[1];
        Language language = Language.valueOf(langName);
        return new PluginScoreboard(genericName, language);
    }
}

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

    public void registerMultiLanguage(String key) {
        for (Language language : Language.values()) {
            register(key + "_" + language.name());
        }
    }

    private boolean keyEndsWithLanguage(String key) {
        boolean endsWithLanguage = false;
        for (Language language : Language.values()) {
            if (key.endsWith("_" + language.name())) {
                endsWithLanguage = true;
                break;
            }
        }
        return endsWithLanguage;
    }

    @Override
    public PluginScoreboard createValueByKey(String name) {
        String[] args = name.split("_");
        String genericName = args[0];
        String langName = args[1];
        Language language = Language.valueOf(langName);
        PluginScoreboard pluginScoreboard = new PluginScoreboard(genericName, language);
        pluginScoreboard.load();
        return pluginScoreboard;
    }
}

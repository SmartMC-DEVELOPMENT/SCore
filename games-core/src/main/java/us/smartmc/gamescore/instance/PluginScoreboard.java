package us.smartmc.gamescore.instance;

import lombok.Getter;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.ScoreboardsManager;

import java.io.File;
import java.util.List;

@Getter
public class PluginScoreboard {

    private static final JavaPlugin plugin = GamesCoreAPI.getApi().getPlugin();
    private static final String ticksUpdateRate = "ticksUpdateRate";
    private static final String lines = "lines";
    private static final String title = "title";

    private final String name;
    private final Language language;

    private SpigotYmlConfig config;

    public PluginScoreboard(String name, Language language) {
        this.name = name;
        this.language = language;
    }

    public void load() {
        File scoreboardsFolder = ScoreboardsManager.getScoreboardsFolder();
        scoreboardsFolder.mkdirs();
        config = new SpigotYmlConfig(new File(scoreboardsFolder, getId() + ".yml"));
        config.register(ticksUpdateRate, 20);
        config.register(title, List.of("&b&lSMARTMC", "&9&lSMARTMC"));
        config.register(lines, List.of("Line1", "Player: <player_name>", "Line3", "&eplay.smartmc.us"));
        config.save();
    }

    public int getTicksUpdateRate() {
        return config.getInt(ticksUpdateRate);
    }

    public List<String> getLines() {
        return config.getStringList(lines);
    }

    public List<String> getTitleLines() {
        return config.getStringList(title);
    }

    public String getId() {
        return name + "_" + language.name();
    }

    private static ScoreboardsManager getManager() {
        return MapManager.getManager(ScoreboardsManager.class);
    }

}

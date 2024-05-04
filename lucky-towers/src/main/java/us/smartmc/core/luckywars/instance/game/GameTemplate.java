package us.smartmc.core.luckywars.instance.game;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import us.smartmc.core.luckywars.LuckyWars;
import us.smartmc.core.luckywars.manager.GameTemplatesManager;

public class GameTemplate {

    private static final GameTemplatesManager manager = LuckyWars.getTemplatesManager();

    @Getter
    private final String name;
    private final FilePluginConfig config;

    @Getter
    private int maxTeamSize;
    @Getter
    private int maxTeamsAmount;

    private GameTemplate(String name) {
        this.name = name;
        this.config = GameTemplatesManager.getConfig(name).load();
        registerConfigDefaults();
    }

    private void registerConfigDefaults() {
        maxTeamSize = config.getInteger("maxTeamsSize", 8);
        maxTeamsAmount = config.getInteger("maxTeamsAmount", 1);
    }

    public static GameTemplate get(String name) {
        GameTemplate template = manager.get(name);
        if (template == null) {
            template = new GameTemplate(name);
            manager.register(name, template);
        }
        return template;
    }

}

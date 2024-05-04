package us.smartmc.core.randomwar.instance.game;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import us.smartmc.core.randomwar.RandomBattle;
import us.smartmc.core.randomwar.manager.GameTemplatesManager;

public class GameTemplate {

    private static final GameTemplatesManager manager = RandomBattle.getTemplatesManager();

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

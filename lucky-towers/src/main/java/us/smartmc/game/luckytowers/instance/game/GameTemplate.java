package us.smartmc.game.luckytowers.instance.game;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.FilePluginConfig;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.manager.GameTemplatesManager;

public class GameTemplate {

    private static final GameTemplatesManager manager = LuckyTowers.getManager(GameTemplatesManager.class);

    @Getter
    private final String name;
    private final FilePluginConfig config;

    @Getter
    private int maxTeamsSize, maxTeamCapacity;
    @Getter
    private int minTeamSize, minTeamsAmount;

    private GameTemplate(String name) {
        this.name = name;
        this.config = GameTemplatesManager.getConfig(name).load();
        registerConfigDefaults();
    }

    private void registerConfigDefaults() {
        maxTeamsSize = config.getInteger("maxTeamsSize", 8);
        maxTeamCapacity = config.getInteger("maxTeamSize", 1);

        minTeamSize = config.getInteger("minTeamSize", 2);

        config.save();
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

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
    private int maxTeamsSize = 8, maxTeamCapacity = 1;
    @Getter
    private int minTeamSize = 2;

    private GameTemplate(String name) {
        this.name = name;
        this.config = GameTemplatesManager.getConfig(name).load();
        registerConfigDefaults();
    }

    private void registerConfigDefaults() {
        maxTeamsSize = config.getInteger("maxTeamsSize", maxTeamsSize);
        maxTeamCapacity = config.getInteger("maxTeamSize", maxTeamCapacity);
        minTeamSize = config.getInteger("minTeamSize", minTeamSize);

        config.put("maxTeamsSize", maxTeamsSize);
        config.put("maxTeamSize", maxTeamCapacity);
        config.put("minTeamSize", minTeamSize);

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

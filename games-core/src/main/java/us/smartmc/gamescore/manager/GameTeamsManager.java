package us.smartmc.gamescore.manager;

import us.smartmc.gamescore.instance.game.GameTeam;
import us.smartmc.gamescore.instance.manager.MapManager;

import java.util.UUID;

public class GameTeamsManager extends MapManager<String, GameTeam> {
    @Override
    public GameTeam createValueByKey(String key) {
        return null;
    }
}

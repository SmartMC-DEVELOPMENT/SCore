package us.smartmc.gamescore.manager.map;

import us.smartmc.gamescore.instance.game.Game;
import us.smartmc.gamescore.instance.game.map.GameMap;
import us.smartmc.gamescore.instance.game.map.GameMapSession;
import us.smartmc.gamescore.instance.manager.MapManager;

import java.util.UUID;

public class GameMapSessionsManager extends MapManager<UUID, GameMapSession> {

    public GameMapSession register(Game game, GameMap map) {
        GameMapSession session = new GameMapSession(game, map);
        put(game.getSessionId(), session);
        return session;
    }

    @Override
    public GameMapSession createValueByKey(UUID id) {
        return null;
    }
}

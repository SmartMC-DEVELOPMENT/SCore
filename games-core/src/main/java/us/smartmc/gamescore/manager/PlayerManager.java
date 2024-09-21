package us.smartmc.gamescore.manager;

import us.smartmc.gamescore.instance.GameCorePlayer;
import us.smartmc.gamescore.instance.manager.MapManager;

import java.util.UUID;

public class PlayerManager extends MapManager<UUID, GameCorePlayer> {

    @Override
    public GameCorePlayer createValueByKey(UUID key) {
        return new GameCorePlayer(key);
    }
}

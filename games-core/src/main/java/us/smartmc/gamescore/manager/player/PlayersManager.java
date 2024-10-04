package us.smartmc.gamescore.manager.player;

import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.instance.manager.MapManager;

import java.util.UUID;

public class PlayersManager extends MapManager<UUID, GameCorePlayer> {

    private PlayersManager(){}

    @Override
    public GameCorePlayer createValueByKey(UUID uuid) {
        return new GameCorePlayer(uuid);
    }
}

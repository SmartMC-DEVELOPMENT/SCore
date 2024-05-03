package us.smartmc.core.randomwar.instance.player;

import lombok.Getter;
import us.smartmc.core.randomwar.RandomWar;
import us.smartmc.core.randomwar.manager.PlayersManager;

import java.util.UUID;

public class GamePlayer {

    private static final RandomWar plugin = RandomWar.getPlugin();
    private static final PlayersManager manager = RandomWar.getPlayersManager();

    @Getter
    private final UUID uuid;

    @Getter
    private final GamePlayerData data;

    private GamePlayer(UUID uuid) {
         this.uuid = uuid;
         this.data = loadData(uuid);
    }

    private static GamePlayerData loadData(UUID uuid) {
        return new GamePlayerData(uuid);
    }

    private static GamePlayer get(UUID uuid) {
        GamePlayer gamePlayer = RandomWar.getPlayersManager().get(uuid);
        if (gamePlayer == null) {
            gamePlayer = new GamePlayer(uuid);
            manager.register(uuid, gamePlayer);
        }
        return gamePlayer;
    }

}

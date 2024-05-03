package us.smartmc.core.randomwar.instance.player;

import lombok.Getter;
import lombok.Setter;
import us.smartmc.core.randomwar.RandomBattle;
import us.smartmc.core.randomwar.instance.game.GameSession;
import us.smartmc.core.randomwar.manager.PlayersManager;

import java.util.UUID;

public class GamePlayer {

    private static final RandomBattle plugin = RandomBattle.getPlugin();
    private static final PlayersManager manager = RandomBattle.getPlayersManager();

    @Getter
    private final UUID uuid;

    @Getter
    private final GamePlayerData data;

    @Setter @Getter
    private GameSession gameSession;

    private GamePlayer(UUID uuid) {
         this.uuid = uuid;
         this.data = loadData(uuid);
    }

    private static GamePlayerData loadData(UUID uuid) {
        return new GamePlayerData(uuid);
    }

    public static GamePlayer get(UUID uuid) {
        GamePlayer gamePlayer = RandomBattle.getPlayersManager().get(uuid);
        if (gamePlayer == null) {
            gamePlayer = new GamePlayer(uuid);
            manager.register(uuid, gamePlayer);
        }
        return gamePlayer;
    }

}

package us.smartmc.gamescore.instance.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.instance.game.Game;
import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.manager.PlayersManager;

@Getter
public abstract class GameCoreGameEvent extends GameCoreEvent {

    private final Game game;

    public GameCoreGameEvent(Game game) {
        this.game = game;
    }
}

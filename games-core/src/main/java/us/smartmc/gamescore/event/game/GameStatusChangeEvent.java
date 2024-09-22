package us.smartmc.gamescore.event.game;

import lombok.Getter;
import us.smartmc.gamescore.instance.event.GameCoreGameEvent;
import us.smartmc.gamescore.instance.game.Game;
import us.smartmc.gamescore.instance.game.GameStatus;

@Getter
public class GameStatusChangeEvent extends GameCoreGameEvent {

    private final GameStatus previousStatus, currentStatus;

    public GameStatusChangeEvent(Game game, GameStatus previousStatus, GameStatus currentStatus) {
        super(game);
        this.previousStatus = previousStatus;
        this.currentStatus = currentStatus;
    }
}

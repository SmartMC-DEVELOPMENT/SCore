package us.smartmc.gamescore.instance.event;

import lombok.Getter;
import us.smartmc.gamescore.instance.game.Game;

@Getter
public abstract class GameCoreGameEvent extends GameCoreEvent {

    private final Game game;

    public GameCoreGameEvent(Game game) {
        this.game = game;
    }
}

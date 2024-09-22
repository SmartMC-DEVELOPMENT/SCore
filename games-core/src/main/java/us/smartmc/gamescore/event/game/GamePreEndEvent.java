package us.smartmc.gamescore.event.game;

import us.smartmc.gamescore.instance.event.GameCoreGameEvent;
import us.smartmc.gamescore.instance.game.Game;

public class GamePreEndEvent extends GameCoreGameEvent {

    public GamePreEndEvent(Game game) {
        super(game);
    }
}

package us.smartmc.gamescore.event.game;

import us.smartmc.gamescore.instance.event.GameCoreGameEvent;
import us.smartmc.gamescore.instance.game.Game;

public class GamePostEndEvent extends GameCoreGameEvent {

    public GamePostEndEvent(Game game) {
        super(game);
    }
}

package us.smartmc.gamescore.event.game;

import us.smartmc.gamescore.instance.event.GameCoreGameEvent;
import us.smartmc.gamescore.instance.game.Game;

public class GamePostStartEvent extends GameCoreGameEvent {

    public GamePostStartEvent(Game game) {
        super(game);
    }
}

package us.smartmc.gamescore.event.game;

import us.smartmc.gamescore.instance.event.GameCoreGameEvent;
import us.smartmc.gamescore.instance.game.Game;

public class GamePreStartEvent extends GameCoreGameEvent {

    public GamePreStartEvent(Game game) {
        super(game);
    }
}

package us.smartmc.core.randomwar.event;

import lombok.Getter;
import us.smartmc.core.randomwar.instance.game.GameSession;

@Getter
public class GameEvent extends GamePluginEvent {

    private final GameSession session;

    public GameEvent(GameSession session) {
        this.session = session;
    }
}

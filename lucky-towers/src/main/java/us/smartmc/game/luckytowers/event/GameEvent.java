package us.smartmc.game.luckytowers.event;

import lombok.Getter;
import us.smartmc.game.luckytowers.instance.game.GameSession;

@Getter
public class GameEvent extends GamePluginEvent {

    private final GameSession session;

    public GameEvent(GameSession session) {
        this.session = session;
    }
}

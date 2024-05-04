package us.smartmc.core.luckywars.event;

import lombok.Getter;
import us.smartmc.core.luckywars.instance.game.GameSession;

@Getter
public class GameEvent extends GamePluginEvent {

    private final GameSession session;

    public GameEvent(GameSession session) {
        this.session = session;
    }
}

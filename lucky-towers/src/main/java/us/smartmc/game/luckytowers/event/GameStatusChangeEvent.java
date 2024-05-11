package us.smartmc.game.luckytowers.event;

import lombok.Getter;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.game.GameSessionStatus;

@Getter
public class GameStatusChangeEvent extends GamePluginEvent {

    private final GameSession session;
    private final GameSessionStatus status;

    public GameStatusChangeEvent(GameSession session, GameSessionStatus status) {
        this.session = session;
        this.status = status;
    }

}

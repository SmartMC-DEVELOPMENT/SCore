package us.smartmc.gamescore.event.player;

import us.smartmc.gamescore.instance.event.GameCorePlayerEvent;
import us.smartmc.gamescore.instance.player.GameCorePlayer;

public class GamePlayerGameLeaveEvent extends GameCorePlayerEvent {

    public GamePlayerGameLeaveEvent(GameCorePlayer corePlayer) {
        super(corePlayer);
    }
}

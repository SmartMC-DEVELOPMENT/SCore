package us.smartmc.gamescore.event.player;

import us.smartmc.gamescore.instance.event.GameCorePlayerEvent;
import us.smartmc.gamescore.instance.player.GameCorePlayer;

public class GamePlayerGameJoinEvent extends GameCorePlayerEvent {

    public GamePlayerGameJoinEvent(GameCorePlayer corePlayer) {
        super(corePlayer);
    }
}

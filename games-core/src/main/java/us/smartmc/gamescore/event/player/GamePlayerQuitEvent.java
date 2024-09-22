package us.smartmc.gamescore.event.player;

import us.smartmc.gamescore.instance.event.GameCorePlayerEvent;
import us.smartmc.gamescore.instance.player.GameCorePlayer;

public class GamePlayerQuitEvent extends GameCorePlayerEvent {

    public GamePlayerQuitEvent(GameCorePlayer corePlayer) {
        super(corePlayer);
    }
}

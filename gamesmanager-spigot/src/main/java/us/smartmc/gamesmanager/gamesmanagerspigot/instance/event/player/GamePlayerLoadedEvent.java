package us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.player;

import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.GamePlayerEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;

public class GamePlayerLoadedEvent extends GamePlayerEvent {

    public GamePlayerLoadedEvent(GamePlayer instance) {
        super(instance);
    }
}

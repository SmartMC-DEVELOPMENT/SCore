package us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.player;

import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.GamePlayerEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;

public class GamePlayerUnloadedEvent extends GamePlayerEvent {

    public GamePlayerUnloadedEvent(GamePlayer instance) {
        super(instance);
    }
}

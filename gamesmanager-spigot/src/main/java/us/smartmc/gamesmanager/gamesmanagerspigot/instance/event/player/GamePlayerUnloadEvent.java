package us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.player;

import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.GamePlayerEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;

public class GamePlayerUnloadEvent extends GamePlayerEvent {

    public GamePlayerUnloadEvent(GamePlayer instance) {
        super(instance);
    }
}

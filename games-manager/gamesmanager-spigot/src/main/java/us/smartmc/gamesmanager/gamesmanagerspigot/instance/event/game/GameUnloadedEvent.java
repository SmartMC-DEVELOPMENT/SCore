package us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.game;

import lombok.Getter;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.GameInstanceEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;

@Getter
public class GameUnloadedEvent extends GameInstanceEvent {

    public GameUnloadedEvent(GameInstance instance) {
        super(instance);
    }
}

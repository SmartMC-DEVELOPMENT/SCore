package us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.game;

import lombok.Getter;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.GameInstanceEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;

@Getter
public class GameLoadedEvent extends GameInstanceEvent {

    public GameLoadedEvent(GameInstance instance) {
        super(instance);
    }
}

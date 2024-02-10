package us.smartmc.gamesmanager.gamesmanagerspigot.instance.event;

import lombok.Getter;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.GameEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;

@Getter
public abstract class GameInstanceEvent extends GameEvent {

    private final GameInstance instance;

    public GameInstanceEvent(GameInstance instance) {
        this.instance = instance;
    }
}

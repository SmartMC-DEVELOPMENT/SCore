package us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.game;

import lombok.Getter;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.GameInstanceEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameStatus;

@Getter
public class GameUnregisterEvent extends GameInstanceEvent {

    public GameUnregisterEvent(GameInstance instance) {
        super(instance);
    }
}

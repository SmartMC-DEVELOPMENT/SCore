package us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.game;

import lombok.Getter;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.GameInstanceEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameStatus;

@Getter
public class GameRegisterEvent extends GameInstanceEvent {

    public GameRegisterEvent(GameInstance instance) {
        super(instance);
    }
}

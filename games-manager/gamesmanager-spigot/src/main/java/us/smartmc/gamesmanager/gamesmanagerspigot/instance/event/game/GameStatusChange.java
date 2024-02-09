package us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.game;

import lombok.Getter;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.GameInstanceEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameStatus;

@Getter
public class GameStatusChange extends GameInstanceEvent {

    private final GameStatus previousStatus, gameStatus;

    public GameStatusChange(GameInstance instance, GameStatus previousStatus, GameStatus gameStatus) {
        super(instance);
        this.previousStatus = previousStatus;
        this.gameStatus = gameStatus;
    }
}

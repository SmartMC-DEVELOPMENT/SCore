package us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.GameInstanceEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameStatus;

@Getter
public class GameStatusChangeEvent extends GameInstanceEvent implements Cancellable {

    @Setter
    private boolean cancelled;
    private final GameStatus previousStatus, gameStatus;

    public GameStatusChangeEvent(GameInstance instance, GameStatus previousStatus, GameStatus gameStatus) {
        super(instance);
        this.previousStatus = previousStatus;
        this.gameStatus = gameStatus;
    }
}

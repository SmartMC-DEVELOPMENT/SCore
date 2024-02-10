package us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.GameInstanceEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameMap;

@Getter
public class GameMapChangeEvent extends GameInstanceEvent implements Cancellable {

    @Setter
    private boolean cancelled;
    private final GameMap previous, map;

    public GameMapChangeEvent(GameInstance instance, GameMap previous, GameMap map) {
        super(instance);
        this.previous = previous;
        this.map = map;
    }
}

package us.smartmc.gamesmanager.gamesmanagerspigot.instance.game;

import lombok.Getter;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.game.GameMapChangeEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.game.GameStatusChangeEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.game.GameStatusChangedEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GameManager;
import us.smartmc.gamesmanager.gamesmanagerspigot.util.BukkitUtil;

@Getter
public abstract class GameInstance implements IGameInstance {

    private final GameManager<?> manager;
    private final String name;
    private GameStatus status = GameStatus.MAINTENANCE;
    private GameMap map;

    public GameInstance(GameManager<?> manager, String name) {
        this.manager = manager;
        this.name = name;
    }

    public void setStatus(GameStatus status) {
        GameStatus previous = this.status;
        GameStatusChangeEvent event = new GameStatusChangeEvent(this, previous, status);
        BukkitUtil.callEvent(event);
        if (event.isCancelled()) return;
        this.status = status;
        BukkitUtil.callEvent(new GameStatusChangedEvent(this, previous, status));
    }

    public void setMap(GameMap map) {
        GameMap previous = this.map;
        GameMapChangeEvent event = new GameMapChangeEvent(this, previous, map);
        BukkitUtil.callEvent(event);
        if (event.isCancelled()) return;
        this.map = map;
        if (previous == null) return;
    }
}

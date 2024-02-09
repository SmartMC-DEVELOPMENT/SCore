package us.smartmc.gamesmanager.gamesmanagerspigot.instance.game;

import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GameManager;

public abstract class GameInstance implements IGameInstance {

    private final GameManager<?> manager;

    private final String name;

    private GameStatus status = GameStatus.MAINTENANCE;

    public GameInstance(GameManager<?> manager, String name) {
        this.manager = manager;
        this.name = name;
    }

    @Override
    public GameStatus getStatus() {
        return status;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public GameManager<?> getManager() {
        return manager;
    }
}

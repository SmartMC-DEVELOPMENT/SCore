package us.smartmc.gamesmanager.gamesmanagerspigot.instance.player;

import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GamePlayerManager;

import java.util.UUID;

public abstract class OfflineGamePlayer implements IOfflineGamePlayer {

    private final GamePlayerManager<?> manager;
    private final UUID uuid;

    public OfflineGamePlayer(GamePlayerManager<?> manager, UUID uuid) {
        this.manager = manager;
        this.uuid = uuid;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public GamePlayerManager<?> getManager() {
        return manager;
    }
}

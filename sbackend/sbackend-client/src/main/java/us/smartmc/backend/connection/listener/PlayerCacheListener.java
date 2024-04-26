package us.smartmc.backend.connection.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.connection.manager.PlayerCacheManager;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.instance.player.PlayerCache;

import java.util.UUID;

public class PlayerCacheListener extends BackendObjectListener<PlayerCache> {

    private final UUID id = UUID.randomUUID();

    public PlayerCacheListener() {
        super(PlayerCache.class);
    }

    @Override
    public void onReceive(ConnectionHandler connection, PlayerCache cache) {
        PlayerCacheManager.parse();
    }
}

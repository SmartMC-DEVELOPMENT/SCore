package us.smartmc.backend.connection.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.connection.manager.PlayerCacheManager;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.instance.player.PlayerCache;

public class PlayerCacheListener extends BackendObjectListener<PlayerCache> {

    public PlayerCacheListener() {
        super(PlayerCache.class);
    }

    @Override
    public void onReceive(ConnectionHandler connection, PlayerCache cache) {
        PlayerCacheManager.parse(cache);
    }
}

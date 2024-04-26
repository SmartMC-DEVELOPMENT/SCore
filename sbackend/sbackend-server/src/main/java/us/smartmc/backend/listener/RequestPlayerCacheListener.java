package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.ModulesHandler;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.instance.player.PlayerCache;
import us.smartmc.backend.protocol.GetPlayerCacheRequest;

import java.util.UUID;

public class RequestPlayerCacheListener extends BackendObjectListener<GetPlayerCacheRequest> {

    public RequestPlayerCacheListener() {
        super(GetPlayerCacheRequest.class);
    }

    @Override
    public void onReceive(ConnectionHandler connection, GetPlayerCacheRequest request) {
        UUID playerId = request.getId();
        PlayerCache cache = ModulesHandler.playersInfoHandler.getCache("testCache." + playerId);
        connection.sendObject(cache);
    }
}

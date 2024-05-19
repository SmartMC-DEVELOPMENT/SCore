package us.smartmc.backend.module;

import us.smartmc.backend.connection.BackendClientConnection;
import us.smartmc.backend.instance.service.BackendService;
import us.smartmc.backend.service.players.common.PlayersServiceContexts;

public class PlayersServiceServer extends BackendService {

    @Override
    public void load() {
        super.load();
    }

    @Override
    public void unload() {
        super.unload();
    }

    public void sendMessage(String username, String message) {
        BackendClientConnection.forEachBackendClient(backendClient -> {
            if (!backendClient.hasContext(PlayersServiceContexts.getUsernameContextId(username))) return;
            backendClient.getConnectionHandler().sendCommand("sendPlayerMsg " + username + " " + message);
        });
    }
}

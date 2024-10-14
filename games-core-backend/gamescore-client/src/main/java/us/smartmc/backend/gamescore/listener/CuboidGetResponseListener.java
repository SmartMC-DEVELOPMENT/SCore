package us.smartmc.backend.gamescore.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.gamescore.BackendConnection;
import us.smartmc.backend.gamescore.CuboidGetResponse;
import us.smartmc.backend.gamescore.CuboidSaveResponse;
import us.smartmc.backend.instance.BackendObjectListener;

public class CuboidGetResponseListener extends BackendObjectListener<CuboidGetResponse> {

    @Override
    public void onReceive(ConnectionHandler connectionHandler, CuboidGetResponse response) {
        BackendConnection.getBackendConnection().ifPresent(client -> {
            client.handleCuboidGetResponse(response);
        });
    }
}

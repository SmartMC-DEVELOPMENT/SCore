package us.smartmc.backend.gamescore.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.gamescore.BackendConnection;
import us.smartmc.backend.gamescore.CuboidSaveResponse;
import us.smartmc.backend.instance.BackendObjectListener;

public class CuboidResponseListener extends BackendObjectListener<CuboidSaveResponse> {

    @Override
    public void onReceive(ConnectionHandler connectionHandler, CuboidSaveResponse response) {
        BackendConnection.getBackendConnection().ifPresent(client -> {
            client.handleCuboidSaveResponse(response);
        });
    }
}

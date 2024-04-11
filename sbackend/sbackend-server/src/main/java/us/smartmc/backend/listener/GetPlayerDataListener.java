package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendUTFListener;

import java.util.UUID;

public class GetPlayerDataListener implements BackendUTFListener {

    @Override
    public void onReceive(ConnectionHandler connection, String utf) {
        if (!utf.startsWith("GETPLAYERDATA")) return;
        UUID id = UUID.fromString(utf.split(" ")[1]);

    }
}

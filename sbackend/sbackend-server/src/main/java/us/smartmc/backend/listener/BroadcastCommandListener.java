
package us.smartmc.backend.listener;

import us.smartmc.backend.connection.BackendClientConnection;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.protocol.BroadcastCommandRequest;
import us.smartmc.backend.protocol.BroadcastRequest;
import us.smartmc.backend.protocol.CommandRequest;

public class BroadcastCommandListener extends BackendObjectListener<BroadcastCommandRequest> {

    public BroadcastCommandListener() {
        super(BroadcastCommandRequest.class);
    }

    @Override
    public void onReceive(ConnectionHandler connection, BroadcastCommandRequest request) {
        ConnectionInputManager.performCommand(connection, new CommandRequest(request.getLabel()));
        BackendClientConnection.forEachBackendClient(client -> {
            perform(client, request);
        });
    }

    private static void perform(BackendClientConnection clientConnection, BroadcastCommandRequest request) {
        String context = request.getContext();
        if (context != null && !clientConnection.hasContext(context)) return;

        String command = request.getLabel();
        clientConnection.getConnectionHandler().sendCommand(command);
    }

}

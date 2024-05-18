
package us.smartmc.backend.listener;

import us.smartmc.backend.connection.BackendClientConnection;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.protocol.BroadcastRequest;
import us.smartmc.backend.protocol.CommandRequest;

public class BroadcastListener extends BackendObjectListener<BroadcastRequest> {

    public BroadcastListener() {
        super(BroadcastRequest.class);
    }

    @Override
    public void onReceive(ConnectionHandler connection, BroadcastRequest request) {
        BackendClientConnection.forEachBackendClient(client -> {
            perform(client, request);
        });
    }

    private static void perform(BackendClientConnection clientConnection, BroadcastRequest request) {
        String context = request.getContext();
        if (context != null && !clientConnection.hasContext(context)) return;

        for (Object arg : request.getArgs()) {
            if (arg instanceof CommandRequest commandRequest) {
                System.out.println("CMD REQUEST" + commandRequest);
            }
            System.out.println("Broadcasting " + arg.toString());
            clientConnection.getConnectionHandler().sendObject(arg);
        }
    }

}

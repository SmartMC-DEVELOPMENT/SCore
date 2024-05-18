package us.smartmc.backend.command;

import us.smartmc.backend.connection.BackendClientConnection;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;

public class SubChannelCommand extends BackendCommandExecutor {

    public SubChannelCommand() {
        super("subChannel");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        BackendClientConnection client = BackendClientConnection.get(connection);
        if (client == null) return;
        client.addChannelSubscription(args[0]);
    }
}

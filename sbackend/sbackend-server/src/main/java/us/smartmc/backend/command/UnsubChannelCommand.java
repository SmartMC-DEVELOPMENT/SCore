package us.smartmc.backend.command;

import us.smartmc.backend.connection.BackendClientConnection;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;

public class UnsubChannelCommand extends BackendCommandExecutor {

    public UnsubChannelCommand() {
        super("unsubChannel");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        BackendClientConnection client = BackendClientConnection.get(connection);
        if (client == null) return;
        client.removeChannelSubscription(args[0]);
        System.out.println(client.getClientName() + " unsub to channel " + args[0]);
    }
}

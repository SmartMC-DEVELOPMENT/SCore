package us.smartmc.backend.command;

import us.smartmc.backend.connection.BackendClientConnection;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;

public class UnsubContextCommand extends BackendCommandExecutor {

    public UnsubContextCommand() {
        super("unsubContext");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        BackendClientConnection client = BackendClientConnection.get(connection);
        if (client == null) return;
        client.removeContextSubscription(args[0]);
        System.out.println(client.getClientName() + " remomed context " + args[0]);
    }
}

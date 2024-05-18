package us.smartmc.core.backend;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;

public class SendPlayerMessageCommand extends BackendCommandExecutor {

    public SendPlayerMessageCommand(String name) {
        super(name);
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {

    }
}

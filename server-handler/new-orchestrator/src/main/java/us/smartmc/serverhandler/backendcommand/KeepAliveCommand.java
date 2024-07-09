package us.smartmc.serverhandler.backendcommand;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;
import us.smartmc.serverhandler.manager.ServerManager;

public class KeepAliveCommand extends BackendCommandExecutor {

    public KeepAliveCommand() {
        super("keepAlive");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        String name = args[0];
        ServerManager.registerAlive(name);
    }
}

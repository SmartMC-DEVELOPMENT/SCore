package us.smartmc.serverhandler.backendcommand;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;
import us.smartmc.serverhandler.manager.VelocityServerManager;

public class RegisterServerCommand extends BackendCommandExecutor {


    public RegisterServerCommand() {
        super("registerServer");
    }

    @Override
    public void onCommand(ConnectionHandler connectionHandler, String label, String[] args) {
        String name = args[0];
        String host = args[1];
        int port = Integer.parseInt(args[2]);
        VelocityServerManager.register(name, host, port);
    }
}

package us.smartmc.serverhandler.backendcommand;

import us.smartmc.backend.instance.BackendCommandExecutor;
import us.smartmc.serverhandler.manager.ServerManager;

public class RegisterServerCommand extends BackendCommandExecutor {

    public RegisterServerCommand() {
        super("registerServer");
    }

    // EXAMPLE: registerServer <name/id> <hostname> <port>
    @Override
    public void onCommand(us.smartmc.backend.connection.ConnectionHandler connection, String label, String[] args) {
        String name = args[0];
        String hostname = args[1];
        int port = Integer.parseInt(args[2]);
        ServerManager.register(name, hostname, port);
        System.out.println("Registered server " + name + " " + hostname + " " + port);
    }
}

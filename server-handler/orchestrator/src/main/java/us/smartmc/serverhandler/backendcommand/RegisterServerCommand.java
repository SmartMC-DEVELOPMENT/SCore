package us.smartmc.serverhandler.backendcommand;

import me.imsergioh.jbackend.api.ConnectionHandler;
import us.smartmc.serverhandler.executor.BackendCommand;
import us.smartmc.serverhandler.instance.CommonServerInfo;
import us.smartmc.serverhandler.instance.ServerStatus;
import us.smartmc.serverhandler.manager.ServerManager;

public class RegisterServerCommand extends BackendCommand {

    public RegisterServerCommand() {
        super("registerServer");
    }

    // EXAMPLE: registerServer <name/id> <hostname> <port>

    @Override
    public void execute(ConnectionHandler handler, String label, String[] args) {
        String name = args[0];
        String hostname = args[1];
        int port = Integer.parseInt(args[2]);
        ServerManager.register(name, hostname, port);
    }
}

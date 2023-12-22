package us.smartmc.serverhandler.backendcommand;

import me.imsergioh.jbackend.api.ConnectionHandler;
import us.smartmc.serverhandler.executor.BackendCommand;
import us.smartmc.serverhandler.manager.BungeeServerManager;

public class RegisterServerCommand extends BackendCommand {


    public RegisterServerCommand() {
        super("registerServer");
    }

    @Override
    public void execute(ConnectionHandler connectionHandler, String label, String[] args) {
        String name = args[0];
        String host = args[1];
        int port = Integer.parseInt(args[2]);
        BungeeServerManager.register(name, host, port);
    }
}

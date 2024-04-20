package us.smartmc.serverhandler.backendcommand;

import me.imsergioh.jbackend.api.ConnectionHandler;
import us.smartmc.serverhandler.executor.BackendCommand;
import us.smartmc.serverhandler.manager.BackendProxyConnectionHandler;
import us.smartmc.serverhandler.manager.ServerManager;

public class KeepAliveCommand extends BackendCommand {

    public KeepAliveCommand() {
        super("keepAlive");
    }

    @Override
    public void execute(ConnectionHandler handler, String label, String[] args) {
        String name = args[0];
        ServerManager.registerAlive(name);
    }
}

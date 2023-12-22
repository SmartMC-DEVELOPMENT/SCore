package us.smartmc.serverhandler.backendcommand;

import me.imsergioh.jbackend.api.ConnectionHandler;
import us.smartmc.serverhandler.executor.BackendCommand;
import us.smartmc.serverhandler.manager.BackendProxyConnectionHandler;
import us.smartmc.serverhandler.manager.ServerManager;

public class IAmAProxyCommand extends BackendCommand {

    public IAmAProxyCommand() {
        super("imAProxy");
    }

    @Override
    public void execute(ConnectionHandler handler, String label, String[] args) {
        BackendProxyConnectionHandler.registerAsProxy(handler);
    }
}

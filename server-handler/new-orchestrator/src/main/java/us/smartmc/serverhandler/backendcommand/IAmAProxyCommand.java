package us.smartmc.serverhandler.backendcommand;

import me.imsergioh.jbackend.api.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;
import us.smartmc.serverhandler.executor.BackendCommand;
import us.smartmc.serverhandler.manager.BackendProxyConnectionHandler;

public class IAmAProxyCommand extends BackendCommandExecutor {

    public IAmAProxyCommand() {
        super("imAProxy");
    }


    @Override
    public void onCommand(us.smartmc.backend.connection.ConnectionHandler connection, String label, String[] args) {
        BackendProxyConnectionHandler.registerAsProxy(connection);
    }
}

package us.smartmc.serverhandler.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.serverhandler.instance.BackendCommandExecuteRequest;
import us.smartmc.serverhandler.manager.BackendCommandManager;


public class ACommandManagerListener extends BackendObjectListener<BackendCommandExecuteRequest> {

    @Override
    public void onReceive(ConnectionHandler connection, BackendCommandExecuteRequest request) {
        BackendCommandManager.perform(connection, request);
    }
}

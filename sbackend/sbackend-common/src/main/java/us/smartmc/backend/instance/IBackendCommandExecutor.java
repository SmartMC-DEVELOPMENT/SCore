package us.smartmc.backend.instance;

import us.smartmc.backend.connection.ConnectionHandler;

public interface IBackendCommandExecutor {

    void onCommand(ConnectionHandler connection, String label, String[] args);

}

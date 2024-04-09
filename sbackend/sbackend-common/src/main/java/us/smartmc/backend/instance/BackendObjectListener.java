package us.smartmc.backend.instance;

import us.smartmc.backend.connection.ConnectionHandler;

public interface BackendObjectListener {

    void onReceive(ConnectionHandler connection, Object object);

}

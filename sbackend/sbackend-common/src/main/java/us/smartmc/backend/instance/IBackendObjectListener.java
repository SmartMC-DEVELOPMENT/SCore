package us.smartmc.backend.instance;

import us.smartmc.backend.connection.ConnectionHandler;

public interface IBackendObjectListener<O> {

    void onReceive(ConnectionHandler connection, O object);

    Class<?> getTypeClass();
}

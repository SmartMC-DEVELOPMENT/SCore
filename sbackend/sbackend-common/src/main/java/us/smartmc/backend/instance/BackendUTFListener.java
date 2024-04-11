package us.smartmc.backend.instance;

import us.smartmc.backend.connection.ConnectionHandler;

public interface BackendUTFListener extends IBackendObjectListener<String> {

    void onReceive(ConnectionHandler connection, String utf);

    @Override
    default Class<?> getTypeClass() {
        return String.class;
    }
}

package us.smartmc.backend.connection.listener;

import us.smartmc.backend.connection.BackendClient;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.instance.cache.CacheCommand;
import us.smartmc.backend.instance.cache.CacheCommandType;
import us.smartmc.backend.protocol.CacheCommandRequest;

import java.util.Objects;

public class CacheCompleteListener extends BackendObjectListener<CacheCommandRequest> {

    @Override
    public void onReceive(ConnectionHandler connection, CacheCommandRequest commandRequest) {
        CacheCommand command = CacheCommandRequest.getCacheCommandFromRequest(commandRequest);
        if (Objects.requireNonNull(command.getType()) == CacheCommandType.SET) {
            BackendClient.mainConnection.completeCache(command.getKey(), command.getValue());
        }
    }
}

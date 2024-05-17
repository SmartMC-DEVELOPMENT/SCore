package us.smartmc.backend.connection.listener;

import us.smartmc.backend.connection.BackendClient;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.instance.cache.CacheCommand;
import us.smartmc.backend.instance.cache.CacheCommandType;

import java.util.Objects;

public class CacheCompleteListener extends BackendObjectListener<CacheCommand> {

    public CacheCompleteListener() {
        super(CacheCommand.class);
    }

    @Override
    public void onReceive(ConnectionHandler connection, CacheCommand command) {
        if (Objects.requireNonNull(command.getType()) == CacheCommandType.UPDATE) {
            BackendClient.mainConnection.completeCache(command.getKey(), command.getValue());
        }
    }
}

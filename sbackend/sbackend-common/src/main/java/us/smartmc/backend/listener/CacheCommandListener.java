package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.CacheManager;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.instance.cache.CacheCommand;
import us.smartmc.backend.protocol.CacheCommandRequest;

public class CacheCommandListener extends BackendObjectListener<CacheCommandRequest> {

    @Override
    public void onReceive(ConnectionHandler connection, CacheCommandRequest commandRequest) {
        CacheCommand command = CacheCommandRequest.getCacheCommandFromRequest(commandRequest);
        CacheManager manager = command.getCacheManager();
        switch (command.getType()) {
            case GET -> {
                connection.setCache(command.getKey(), manager.get(command.getKey()));
            }
            case SET -> {
                manager.put(command.getKey(), command.getValue());
            }
            case REGISTER -> {
                manager.register(command.getKey(), command.getValue());
            }
            case REMOVE -> {
                manager.remove(command.getKey());
            }
        }
    }
}

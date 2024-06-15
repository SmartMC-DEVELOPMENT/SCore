package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.CacheManager;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.instance.cache.CacheCommand;
import us.smartmc.backend.instance.cache.CacheCommandType;

public class CacheCommandListener extends BackendObjectListener<CacheCommand> {

    @Override
    public void onReceive(ConnectionHandler connection, CacheCommand command) {
        CacheManager manager = command.getCacheManager();
        switch (command.getType()) {
            case GET -> {
                connection.sendObject(CacheCommand.build(CacheCommandType.UPDATE, command.getKey()).value(manager.get(command.getKey())));
            }
            case UPDATE -> {
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

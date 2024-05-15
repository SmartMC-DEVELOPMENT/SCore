package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.CacheManager;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.instance.cache.CacheCommand;
import us.smartmc.backend.instance.cache.CacheCommandType;

public class CacheCommandListener extends BackendObjectListener<CacheCommand> {

    public CacheCommandListener() {
        super(CacheCommand.class);
    }

    @Override
    public void onReceive(ConnectionHandler connection, CacheCommand command) {
        CacheManager manager = command.getCacheManager();
        System.out.println("CACHECOMMAND " + manager.getId() + " " + command.getType().name() + "!");
        switch (command.getType()) {
            case GET -> {
                connection.sendObject(CacheCommand.build(CacheCommandType.UPDATE, command.getKey()).value(manager.get(command.getKey())));
                System.out.println("GET AND SENT!");
            }
            case UPDATE -> {
                manager.put(command.getKey(), command.getValue());
                System.out.println("UPDATED " + command.getKey() + " to " + command.getValue());
            }
            case REGISTER -> {
                boolean registered = manager.register(command.getKey(), command.getValue());
                System.out.println("REGISTERING " + command.getKey() + " " + command.getValue() + " (" + registered + ")");
            }
            case REMOVE -> {
                manager.remove(command.getKey());
                System.out.println("REMOVED " + command.getKey() + "!");
            }
        }
    }
}

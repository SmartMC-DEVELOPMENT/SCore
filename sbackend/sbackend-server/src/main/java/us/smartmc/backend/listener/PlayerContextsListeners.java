package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.PlayerContextsHandler;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.instance.handler.MapHandler;
import us.smartmc.backend.instance.player.PlayerCache;
import us.smartmc.backend.protocol.PlayerContextRequest;
import us.smartmc.backend.util.ConsoleUtil;

public class PlayerContextsListeners extends BackendObjectListener<PlayerContextRequest> {

    public PlayerContextsListeners() {
        super(PlayerContextRequest.class);
    }

    @Override
    public void onReceive(ConnectionHandler connection, PlayerContextRequest request) {
        switch (request.getType()) {
            default -> ConsoleUtil.print("&4Request received PlayerContext with unknown type");
            case SET_CACHE -> {
                Object[] args = request.getArgs();
                getCache(request).set(args[0], args[1]);
                ConsoleUtil.print("&aPlayerCache has been set &e" + args[0] + "&a to &b" + args[1] + "&a!");
            }
            case REMOVE_CACHE -> {
                Object[] args = request.getArgs();
                getCache(request).remove(args[0]);
                ConsoleUtil.print("&aPlayerCache has been removed &e" + args[0] + "&a!");
            }
        }
    }

    private PlayerCache getCache(PlayerContextRequest request) {
        return MapHandler.getHandler(PlayerContextsHandler.class).getOrCreate(request.getPlayerId()).getCache();
    }

}


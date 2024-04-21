package us.smartmc.backend.command;

import com.google.gson.Gson;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.ModulesHandler;
import us.smartmc.backend.handler.PlayersInfoHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;
import us.smartmc.backend.instance.handler.MapHandler;
import us.smartmc.backend.instance.player.PlayerCache;

import java.util.UUID;

public class GetPlayerCacheCmd extends BackendCommandExecutor {

    public GetPlayerCacheCmd() {
        super("getPlayerCache");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        UUID playerId = UUID.fromString(args[0]);
        PlayerCache cache = ModulesHandler.playersInfoHandler.getCache(playerId);
        connection.sendCommand("setPlayerCache " + new Gson().toJson(cache.getCacheMap()));
    }
}

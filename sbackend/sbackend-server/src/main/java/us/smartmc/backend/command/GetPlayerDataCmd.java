package us.smartmc.backend.command;

import com.google.gson.Gson;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.PlayerContextsHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;

import java.util.UUID;

public class GetPlayerDataCmd extends BackendCommandExecutor {

    public GetPlayerDataCmd() {
        super("getPlayerData");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        UUID id = UUID.fromString(args[0]);
        String data = new Gson().toJson(PlayerContextsHandler.getOrCreate(id).getCache().getCacheMap());
        connection.sendCommand("setPlayerData " + data);
    }
}

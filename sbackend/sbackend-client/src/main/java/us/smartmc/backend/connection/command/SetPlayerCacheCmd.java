package us.smartmc.backend.connection.command;

import com.google.gson.Gson;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.connection.manager.PlayerCacheManager;
import us.smartmc.backend.instance.BackendCommandExecutor;
import us.smartmc.backend.instance.player.PlayerCache;

import java.util.Map;
import java.util.UUID;

public class SetPlayerCacheCmd extends BackendCommandExecutor {

    public SetPlayerCacheCmd() {
        super("setPlayerCache");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        String json = label.replaceFirst(getName() + " ", "");
        Map<?, ?> dataMap = new Gson().fromJson(json, Map.class);
        PlayerCacheManager.parse(new PlayerCache(UUID.fromString((String) dataMap.get("_id"))));
    }
}

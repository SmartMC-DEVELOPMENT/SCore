package us.smartmc.backend.connection.command;

import com.google.gson.Gson;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class SetPlayerDataCmd extends BackendCommandExecutor {

    public SetPlayerDataCmd() {
        super("setPlayerData");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        String json = label.replaceFirst(getName() + " ", "");
        Map<?, ?> dataMap = new Gson().fromJson(json, Map.class);

    }
}

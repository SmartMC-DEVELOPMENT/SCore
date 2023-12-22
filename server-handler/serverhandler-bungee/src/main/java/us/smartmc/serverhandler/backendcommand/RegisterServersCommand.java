package us.smartmc.serverhandler.backendcommand;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import me.imsergioh.jbackend.api.ConnectionHandler;
import us.smartmc.serverhandler.executor.BackendCommand;
import us.smartmc.serverhandler.manager.BungeeServerManager;

import java.util.Set;

public class RegisterServersCommand extends BackendCommand {


    public RegisterServersCommand() {
        super("registerServers");
    }

    @Override
    public void execute(ConnectionHandler connectionHandler, String label, String[] args) {
        String jsonText = label.split(" ")[1];
        Set<LinkedTreeMap<String, Object>> servers = new Gson().fromJson(jsonText, Set.class);
        servers.forEach(serverInfo -> {
            String name = (String) serverInfo.get("name");
            String hostname = (String) serverInfo.get("host");
            int port = ((Number) serverInfo.get("port")).intValue();
            BungeeServerManager.register(name, hostname, port);
        });
    }
}

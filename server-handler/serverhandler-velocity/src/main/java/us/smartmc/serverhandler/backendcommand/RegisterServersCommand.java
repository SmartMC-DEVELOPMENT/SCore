package us.smartmc.serverhandler.backendcommand;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;
import us.smartmc.serverhandler.manager.VelocityServerManager;

import java.util.Set;

public class RegisterServersCommand extends BackendCommandExecutor {


    public RegisterServersCommand() {
        super("registerServers");
    }

    @Override
    public void onCommand(ConnectionHandler connectionHandler, String label, String[] args) {
        String jsonText = label.split(" ")[1];
        Set<LinkedTreeMap<String, Object>> servers = new Gson().fromJson(jsonText, Set.class);
        servers.forEach(serverInfo -> {
            String name = (String) serverInfo.get("name");
            String hostname = (String) serverInfo.get("host");
            int port = ((Number) serverInfo.get("port")).intValue();
            VelocityServerManager.register(name, hostname, port);
        });
    }
}

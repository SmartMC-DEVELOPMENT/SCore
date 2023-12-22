package us.smartmc.serverhandler.backendcommand;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import me.imsergioh.jbackend.api.ConnectionHandler;
import us.smartmc.serverhandler.ServerHandlerMain;
import us.smartmc.serverhandler.executor.BackendCommand;
import us.smartmc.serverhandler.manager.BungeeServerManager;

import java.util.Set;

public class UnregisterServerCommand extends BackendCommand {


    public UnregisterServerCommand() {
        super("unregisterServer");
    }

    @Override
    public void execute(ConnectionHandler connectionHandler, String label, String[] args) {
        String name = args[0];

        // TODO: CONNECT TO SAME TYPE OF SERVER OR ANOTHER AVAILABLE (MAYBE: CREATE FALLBACK SERVER)
        ServerHandlerMain.proxy.getServerInfo(name).getPlayers().forEach(player -> {
            player.disconnect("Disconnected from the server. F in the chat");
        });

        ServerHandlerMain.proxy.getServers().remove(name);
    }
}

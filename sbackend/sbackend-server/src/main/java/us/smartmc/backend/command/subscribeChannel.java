package us.smartmc.backend.command;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.ModulesHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;
import us.smartmc.backend.instance.server.ServerInfo;

public class subscribeChannel extends BackendCommandExecutor {

    public subscribeChannel() {
        super("updateServerInfo");
    }

    @Override
    public void onCommand(ConnectionHandler connection, String label, String[] args) {
        String serverId = args[0];
        ServerInfo serverInfo = ModulesHandler.serversInfoHandler.get(serverId);
        String commandArg = args[1].toLowerCase();

        switch (commandArg) {
            // example: updateServerInfo <serverId> register <port> <maxPlayers>
            case "register" -> {
                int port = Integer.parseInt(args[2]);
                int maxPlayers = Integer.parseInt(args[3]);
                serverInfo = ModulesHandler.serversInfoHandler.getDefaultValue(serverId, port, maxPlayers);

            }
            // example: updateServerInfo <serverId> count <int>
            case "count" -> {
                int count = Integer.parseInt(args[2]);
                serverInfo.setOnlineCount(count);
            }
        }
    }
}

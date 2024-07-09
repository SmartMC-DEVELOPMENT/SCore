
package us.smartmc.serverhandler.manager;

import us.smartmc.backend.connection.ConnectionHandler;

import java.util.HashSet;
import java.util.Set;

public class BackendProxyConnectionHandler {

    private static final Set<ConnectionHandler> proxyConnections = new HashSet<>();

    public static void registerAsProxy(ConnectionHandler connectionHandler) {
        proxyConnections.add(connectionHandler);
        System.out.println("New proxy has been connected!");
        sendAllCurrentOnlineServers(connectionHandler);
    }

    public static void broadcast(String command) {
        proxyConnections.forEach(connectionHandler -> {
            checkStatus(connectionHandler);
            System.out.println("Broadcasting to proxy... (" + command + ")");
            connectionHandler.sendCommand(command);
        });
    }

    private static void sendAllCurrentOnlineServers(ConnectionHandler handler) {
        String jsonTextServers = ServerManager.getServersAsJsonText();
        handler.sendCommand("registerServers " + jsonTextServers);
    }

    private static void checkStatus(ConnectionHandler connectionHandler) {
        if (connectionHandler.getConnection().isClosed()) {
            proxyConnections.remove(connectionHandler);
        }
    }

}

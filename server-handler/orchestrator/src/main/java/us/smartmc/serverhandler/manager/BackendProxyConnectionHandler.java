
package us.smartmc.serverhandler.manager;

import me.imsergioh.jbackend.api.ConnectionHandler;
import me.imsergioh.jbackend.api.manager.BackendActionManager;
import us.smartmc.serverhandler.util.ConnectionUtil;

import java.util.HashSet;
import java.util.Set;

public class BackendProxyConnectionHandler {

    private static final Set<ConnectionHandler> proxyConnections = new HashSet<>();

    public static void registerAsProxy(ConnectionHandler connectionHandler) {
        proxyConnections.add(connectionHandler);
        BackendActionManager.registerDisconnectAction(proxyConnections::remove);
        System.out.println("New proxy has been connected!");
        sendAllCurrentOnlineServers(connectionHandler);
    }

    public static void broadcast(String command) {
        proxyConnections.forEach(h -> {
            System.out.println("Broadcasting to proxy...");
            ConnectionUtil.sendCommand(h, command);
        });
    }

    private static void sendAllCurrentOnlineServers(ConnectionHandler handler) {
        String jsonTextServers = ServerManager.getServersAsJsonText();
        ConnectionUtil.sendCommand(handler, "registerServers " + jsonTextServers);
    }

}

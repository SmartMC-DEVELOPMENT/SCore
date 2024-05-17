package us.smartmc.backend.connection;

import lombok.Getter;
import us.smartmc.backend.protocol.LoginCompleted;
import us.smartmc.backend.util.ConsoleUtil;

import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@Getter
public class BackendClientConnection {

    private static final Map<String, BackendClientConnection> connections = new HashMap<>();

    private final ConnectionHandler connectionHandler;
    private final String clientName;

    private final Set<String> channelSubscriptions = new HashSet<>();

    public BackendClientConnection(ConnectionHandler connectionHandler, String username) {
        this.connectionHandler = connectionHandler;
        this.clientName = getClientName(username);
        connections.put(clientName, this);
        connectionHandler.sendObject(new LoginCompleted(clientName));
        ConsoleUtil.print("New client connection logged in! (" + clientName + ")");
    }

    public void addChannelSubscription(String channelId) {
        channelSubscriptions.add(channelId);
    }

    public void removeChannelSubscription(String channelId) {
        channelSubscriptions.remove(channelId);
    }

    public boolean isSuscriptorOf(String channelId) {
        return channelSubscriptions.contains(channelId);
    }

    public static String getClientName(String username) {
        int id = 0;
        String name = username + "-" + id;
        while (connections.containsKey(name)) {
            id++;
            name = username + "-" + id;
        }
        return name;
    }

    public static void removeConnection(Socket socket) {
        String clientName = null;
        for (BackendClientConnection connection : connections.values()) {
            if (!connection.getConnectionHandler().getConnection().equals(socket)) continue;
            clientName = connection.getClientName();
            break;
        }
        if (clientName == null) return;
        connections.remove(clientName);
        System.out.println(clientName + " disconnected!");
    }

    public static BackendClientConnection get(ConnectionHandler handler) {
        Socket socket = handler.getConnection();
        for (BackendClientConnection connection : connections.values()) {
            if (!connection.getConnectionHandler().getConnection().equals(socket)) continue;
            return connection;
        }
        return null;
    }

    public static void forEachBackendClient(Consumer<BackendClientConnection> consumer) {
        connections.values().forEach(consumer);
    }

}

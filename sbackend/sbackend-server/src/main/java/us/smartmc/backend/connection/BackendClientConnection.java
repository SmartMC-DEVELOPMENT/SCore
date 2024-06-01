package us.smartmc.backend.connection;

import lombok.Getter;
import us.smartmc.backend.protocol.LoginCompleted;
import us.smartmc.backend.util.ConsoleUtil;

import java.net.Socket;
import java.util.*;
import java.util.function.Consumer;

@Getter
public class BackendClientConnection {

    private static final Map<String, BackendClientConnection> connections = new HashMap<>();

    private final ConnectionHandler connectionHandler;
    private final String clientName;

    private final Set<String> contextSubscriptions = new HashSet<>();

    public BackendClientConnection(ConnectionHandler connectionHandler, String username) {
        this.connectionHandler = connectionHandler;
        this.clientName = getClientName(username);
        connections.put(clientName, this);
        connectionHandler.sendObject(new LoginCompleted(clientName));
        ConsoleUtil.print("New client connection logged in! (" + clientName + ")");
    }

    public void addChannelSubscription(String channelId) {
        addContextSubscription(getChannelSubscriptionId(channelId));
    }

    public void removeChannelSubscription(String channelId) {
        removeContextSubscription(getChannelSubscriptionId(channelId));
    }

    public boolean isChannelSubscriptorOf(String channelId) {
        return hasContext(getChannelSubscriptionId(channelId));
    }

    public void addContextSubscription(String contextId) {
        contextSubscriptions.add(contextId);
    }

    public void removeContextSubscription(String contextId) {
        contextSubscriptions.remove(contextId);
    }

    public boolean hasContext(String contextId) {
        return contextSubscriptions.contains(contextId);
    }

    public static String getClientName(String username) {
        return username + "-" + new Random().nextInt(9999);
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

    private static String getChannelSubscriptionId(String name) {
        return "channel@" + name;
    }

}

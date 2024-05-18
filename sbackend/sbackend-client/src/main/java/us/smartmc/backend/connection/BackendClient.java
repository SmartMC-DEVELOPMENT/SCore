package us.smartmc.backend.connection;

import lombok.Getter;
import us.smartmc.backend.connection.listener.LoginCompleteListener;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.protocol.BroadcastCommandRequest;
import us.smartmc.backend.protocol.BroadcastRequest;
import us.smartmc.backend.protocol.CommandRequest;
import us.smartmc.backend.protocol.LoginRequest;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Getter
public class BackendClient extends ConnectionHandler {

    public static BackendClient mainConnection;
    private static ClientReconnectionTimer reconnectionTimer;

    private String user, password;

    private final Map<String, Consumer<Object>> pendingGets = new HashMap<>();

    public BackendClient(String hostname, int port) throws IOException {
        super(new Socket(hostname, port));
        ConnectionInputManager.registerListeners(new LoginCompleteListener());
        mainConnection = this;
    }

    public void broadcastCommand(String context, String command) {
        BroadcastCommandRequest request = new BroadcastCommandRequest(context, command);
        sendObject(request);
    }

    public void broadcast(String context, Object... args) {
        BroadcastRequest request = new BroadcastRequest(context, args);
        sendObject(request);
    }

    public void subscribeContext(String contextId) {
        sendCommand("subContext " + contextId);
    }

    public void unsubscribeContext(String contextId) {
        sendCommand("unsubContext " + contextId);
    }

    public void subscribeChannel(String channelId) {
        sendCommand("subChannel " + channelId);
    }

    public void unsubscribeChannel(String channelId) {
        sendCommand("unsubChannel " + channelId);
    }

    public void login(String username, String password) {
        this.user = username;
        this.password = password;
        sendObject(new LoginRequest(username, password));
    }

    public void completeCache(String key, Object value) {
        Consumer<Object> consumer = pendingGets.get(key);
        if (consumer == null) return;
        consumer.accept(value);
    }

    @Override
    public void getCache(String key, Consumer<Object> consumer) {
        super.getCache(key, consumer);
        pendingGets.put(key, consumer);
    }

    public void relogin() {
        try {
            BackendClient client = new BackendClient(connection.getInetAddress().getHostAddress(), connection.getPort());
            mainConnection = client;
            client.login(user, password);
            new Thread(client).start();
            reconnectionTimer.finish();
        } catch (IOException e) {
            handleException(e);
        }
        System.out.println("Relogging into backend...");
    }

    private void registerSuccessLogin() {
        if (reconnectionTimer != null) reconnectionTimer.finish();
    }

    @Override
    public void handleException(Exception e) {
        if (e instanceof EOFException) {
            reconnectionTimer = new ClientReconnectionTimer(this);
        }
        e.printStackTrace();
    }
}

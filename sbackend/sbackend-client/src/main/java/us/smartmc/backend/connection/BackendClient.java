package us.smartmc.backend.connection;

import lombok.Getter;
import us.smartmc.backend.connection.listener.LoginCompleteListener;
import us.smartmc.backend.handler.CacheManager;
import us.smartmc.backend.handler.ConnectionInputManager;
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
        if (mainConnection == null) mainConnection = this;
    }

    public void subscribe(String channelId) {
        sendCommand("subChannel " + channelId);
    }

    public void unsubscribe(String channelId) {
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

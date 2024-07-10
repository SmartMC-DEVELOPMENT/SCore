package us.smartmc.backend.connection;

import lombok.Getter;
import us.smartmc.backend.connection.listener.LoginCompleteListener;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.protocol.BroadcastCommandRequest;
import us.smartmc.backend.protocol.BroadcastRequest;
import us.smartmc.backend.protocol.LoginRequest;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

@Getter
public class BackendClient extends ConnectionHandler {

    private final ClientReconnectionTask reconnectionTimer;
    private String user, password;

    public BackendClient(String hostname, int port) throws IOException {
        super(new Socket(hostname, port));
        ConnectionInputManager.registerListeners(new LoginCompleteListener(this));
        reconnectionTimer = new ClientReconnectionTask(this);
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

    public void relogin() {
        try {
            BackendClient client = new BackendClient(connection.getInetAddress().getHostAddress(), connection.getPort());
            client.login(user, password);
            new Thread(client).start();
        } catch (IOException e) {
            this.handleException(e);
        }
        System.out.println("Relogging into backend...");
    }

    private void registerSuccessLogin() {
        if (reconnectionTimer != null) reconnectionTimer.successLogin();
    }

    @Override
    public void handleException(Exception e) {
        if (!connection.isConnected()) {
            reconnectionTimer.startReconnectionTask();
        }
        e.printStackTrace();
    }
}

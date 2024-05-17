package us.smartmc.backend.connection;

import lombok.Getter;
import us.smartmc.backend.connection.listener.LoginCompleteListener;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.protocol.LoginRequest;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class BackendClient extends ConnectionHandler {

    public static BackendClient mainConnection;

    @Getter
    private long lastSuccesLogin;
    private String user, password;

    public BackendClient(String hostname, int port) throws IOException {
        super(new Socket(hostname, port));
        if (mainConnection == null) mainConnection = this;
        ConnectionInputManager.registerListeners(new LoginCompleteListener());
    }

    public void login(String username, String password) {
        this.user = username;
        this.password = password;
        sendObject(new LoginRequest(username, password));
    }

    public void relogin() {
        try {
            connection = new Socket(connection.getInetAddress().getHostAddress(), connection.getPort());
        } catch (IOException e) {
            handleException(e);
        }
        System.out.println("Relogging into backend...");
        login(user, password);
    }

    private void registerSuccessLogin() {
        lastSuccesLogin = System.currentTimeMillis();
    }

    @Override
    public void handleException(Exception e) {
        if (e instanceof EOFException) {
            new ClientReconnectionTimer(this);
        }
        e.printStackTrace();
    }
}

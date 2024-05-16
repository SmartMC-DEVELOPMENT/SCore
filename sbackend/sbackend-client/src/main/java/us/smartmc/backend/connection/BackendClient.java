package us.smartmc.backend.connection;

import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.protocol.LoginRequest;

import java.io.IOException;
import java.net.Socket;

public class BackendClient extends ConnectionHandler {

    public static BackendClient mainConnection;

    public BackendClient(String hostname, int port) throws IOException {
        super(getSocketConection(hostname, port));
        if (mainConnection == null) mainConnection = this;
    }

    public void login(String username, String password) {
        sendObject(new LoginRequest(username, password));
    }

    private static Socket getSocketConection(String hostname, int port) {
        try {
            return new Socket(hostname, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleException(Exception e) {
        e.printStackTrace();
    }
}

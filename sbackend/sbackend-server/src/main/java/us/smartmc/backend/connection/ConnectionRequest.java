package us.smartmc.backend.connection;

import us.smartmc.backend.handler.AuthHandler;
import us.smartmc.backend.protocol.LoginRequest;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ConnectionRequest extends Thread {

    private final SSLSocket connection;

    public ConnectionRequest(SSLSocket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(connection.getInputStream());
            LoginRequest loginRequest = (LoginRequest) inputStream.readObject();
            if (!AuthHandler.checkLogin(loginRequest.username(), loginRequest.password())) return;
            new BackendClientConnection(connection).start();
        } catch (Exception e) {
            try {
                connection.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }
}

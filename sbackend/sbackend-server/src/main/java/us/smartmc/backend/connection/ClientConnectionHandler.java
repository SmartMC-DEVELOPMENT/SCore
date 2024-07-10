package us.smartmc.backend.connection;

import us.smartmc.backend.handler.LoginAuthManager;
import us.smartmc.backend.protocol.LoginRequest;

import java.io.IOException;
import java.net.Socket;

public class ClientConnectionHandler extends ConnectionHandler {

    public ClientConnectionHandler(Socket connection) throws IOException {
        super(connection);
    }

    @Override
    public void run() {
        try {
            Object o = inputStream.readObject();
            if (o instanceof LoginRequest loginRequest) {
                if (!LoginAuthManager.checkLogin(loginRequest.getUsername(), loginRequest.getPassword())) {
                    System.out.println("FAILED LOGIN " + loginRequest.getUsername() + " " + loginRequest.getPassword());
                    disconectConnection(connection);
                    return;
                }
                new BackendClientConnection(this, loginRequest.getUsername());
                super.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
            disconectConnection(connection);
        }
    }

    @Override
    public void handleException(Exception e) {
        if (e instanceof IOException) {
            try {
                inputStream.close();
                outputStream.close();
                connection.close();
                BackendClientConnection.removeConnection(connection);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            e.printStackTrace();
        }
        System.out.println("Connection from " + connection.getInetAddress().getHostAddress() + " has been closed!");
    }

    public void disconectConnection(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Disconnected connection request " + socket.getInetAddress().getHostAddress());
    }
}

package us.smartmc.backend.connection;

import us.smartmc.backend.handler.AuthHandler;
import us.smartmc.backend.protocol.LoginRequest;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ClientConnectionHandler extends ConnectionHandler {

    public ClientConnectionHandler(Socket connection) throws IOException {
        super(connection);
    }

    @Override
    public void run() {
        try {
            Object o = inputStream.readObject(true);
            if (o instanceof LoginRequest loginRequest) {
                if (!AuthHandler.checkLogin(loginRequest.getUsername(), loginRequest.getPassword())) {
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
        if (e.getClass().equals(IOException.class) || e.getClass().equals(SocketException.class)) {
            try {
                inputStream.close();
                outputStream.close();
                connection.close();
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

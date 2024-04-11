package us.smartmc.backend.connection;

import us.smartmc.backend.handler.AuthHandler;
import us.smartmc.backend.protocol.LoginRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ConnectionRequest extends ConnectionHandler {

    public ConnectionRequest(Socket connection) throws IOException {
        super(connection);
    }

    @Override
    public void run() {
        try {
            Object o = inputStream.getInputStream().readObject();
            System.out.println("Received connectionrequest " + o);
            if (o instanceof LoginRequest loginRequest) {
                System.out.println("AUTH " + loginRequest.getUsername() + " " + loginRequest.getPassword());
                if (!AuthHandler.checkLogin(loginRequest.getUsername(), loginRequest.getPassword())) {
                    disconectConnection(connection);
                    return;
                }
                new Thread(new BackendClientConnection(this, loginRequest.getUsername())).start();
                super.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
            disconectConnection(connection);
        }
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

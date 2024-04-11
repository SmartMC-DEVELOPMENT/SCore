package us.smartmc.backend.connection;

import lombok.Getter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Getter
public class BackendServer {

    private final ServerSocket serverSocket;

    public BackendServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Servidor iniciado en el puerto " + port + "...");
        while (true) {
            Socket socket = serverSocket.accept();
            acceptConnection(socket);
        }
    }

    private void acceptConnection(Socket socket) {
        System.out.println("New ConnectionRequest -> " + socket.getInetAddress().getHostAddress());
        try {
            new Thread(new ConnectionRequest(socket)).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

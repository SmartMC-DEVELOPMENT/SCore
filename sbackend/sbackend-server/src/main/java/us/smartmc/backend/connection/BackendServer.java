package us.smartmc.backend.connection;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Getter
public class BackendServer extends Thread {

    @Setter
    private boolean active = true;
    private final ServerSocket serverSocket;

    public BackendServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Servidor iniciado en el puerto " + port + "...");
    }

    @Override
    public void run() {
        while (active) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            acceptConnection(socket);
        }
    }

    private void acceptConnection(Socket socket) {
        System.out.println("New ConnectionRequest -> " + socket.getInetAddress().getHostAddress());
        try {
            new Thread(new ClientConnectionHandler(socket)).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

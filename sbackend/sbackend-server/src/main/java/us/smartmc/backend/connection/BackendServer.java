package us.smartmc.backend.connection;

import lombok.Getter;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;

@Getter
public class BackendServer {

    private final SSLServerSocket serverSocket;

    public BackendServer(SSLServerSocketFactory serverSocketFactory, int port) throws IOException {
        serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(port);

        serverSocket.setNeedClientAuth(false);
        serverSocket.setWantClientAuth(false);

        System.out.println("Servidor SSL iniciado en el puerto " + port + "...");

        while (true) {
            SSLSocket socket = (SSLSocket) serverSocket.accept();
            acceptConnection(socket);
        }
    }

    private void acceptConnection(SSLSocket socket) {
        new ConnectionRequest(socket).start();
    }

}

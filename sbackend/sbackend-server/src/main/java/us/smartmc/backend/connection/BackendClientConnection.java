package us.smartmc.backend.connection;

import lombok.Getter;

import java.io.IOException;
import java.net.Socket;

@Getter
public class BackendClientConnection extends ConnectionHandler {

    private final String clientName;

    public BackendClientConnection(ConnectionHandler connectionHandler, String clientName) {
        super(connectionHandler.getConnection(), connectionHandler.getOutputStream(), connectionHandler.getInputStream());
        this.clientName = clientName;
        System.out.println("New client connection from " + clientName);
        try {
            outputStream.writeUTFMessage("Hola mundo! Soy la consola y soy el que mada aqui el KING");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

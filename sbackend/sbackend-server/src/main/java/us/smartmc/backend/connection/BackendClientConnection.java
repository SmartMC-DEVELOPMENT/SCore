package us.smartmc.backend.connection;

import lombok.Getter;
import us.smartmc.backend.protocol.CommandRequest;

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
            outputStream.writeCommand("test arg0 arg1 etc");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package us.smartmc.backend.connection;

import lombok.Getter;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ConnectionInputStream extends ObjectInputStream {

    @Getter
    private final ConnectionHandler connectionHandler;

    public ConnectionInputStream(ConnectionHandler connectionHandler) throws IOException {
        this.connectionHandler = connectionHandler;
    }

}

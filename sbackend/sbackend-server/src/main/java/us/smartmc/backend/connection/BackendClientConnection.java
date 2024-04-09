package us.smartmc.backend.connection;

import lombok.Getter;

import javax.net.ssl.SSLSocket;

@Getter
public class BackendClientConnection extends ConnectionHandler {

    public BackendClientConnection(SSLSocket connection) {
        super(connection);
    }

    @Override
    public void run() {

    }
}

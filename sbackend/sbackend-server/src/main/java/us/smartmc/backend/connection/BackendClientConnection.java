package us.smartmc.backend.connection;

import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
public class BackendClientConnection {

    private static final Map<String, BackendClientConnection> connections = new HashMap<>();

    private final ConnectionHandler connectionHandler;
    private final String clientName;

    public BackendClientConnection(ConnectionHandler connectionHandler, String username) {
        this.connectionHandler = connectionHandler;
        this.clientName = getClientName(username);
        connections.put(clientName, this);
        System.out.println("New client connection logged in! (" + clientName + ")");
    }

    public static String getClientName(String username) {
        int id = 0;
        String name = username + "-" + id;
        while (connections.containsKey(name)) {
            id++;
            name = username + "-" + id;
        }
        return name;
    }

}

package us.smartmc.backend.handler;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendCommandExecutor;
import us.smartmc.backend.instance.IBackendCommandExecutor;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.protocol.CommandRequest;

import java.util.*;

public class ConnectionInputManager {

    private static final Map<String, IBackendCommandExecutor> commands = new HashMap<>();
    private static final Set<BackendObjectListener> listeners = new HashSet<>();

    public static void performCommand(ConnectionHandler connection, CommandRequest request) {
        String name = request.getName();
        IBackendCommandExecutor executor = commands.get(name);
        if (executor == null) {
            System.out.println("Received unknown command from " + connection.getConnection().getInetAddress() + " " + request.getLabel());
            return;
        }
        commands.get(name).onCommand(connection, request.getLabel(), request.getArgs());
    }

    public static void performListener(ConnectionHandler connection, Object object) {
        for (BackendObjectListener listener : listeners) {
            listener.onReceive(connection, object);
        }
    }

    public static void registerCommands(BackendCommandExecutor... executors) {
        for (BackendCommandExecutor executor : executors) {
            commands.put(executor.getName(), executor);
        }
    }

    public static void registerListeners(BackendObjectListener... listeners) {
        ConnectionInputManager.listeners.addAll(Arrays.asList(listeners));
    }
}

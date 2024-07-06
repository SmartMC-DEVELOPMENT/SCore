package us.smartmc.backend.connection;

import lombok.Getter;
import us.smartmc.backend.command.ServiceCommand;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.cache.CacheCommand;
import us.smartmc.backend.instance.cache.CacheCommandType;
import us.smartmc.backend.instance.messaging.MessageCommand;
import us.smartmc.backend.listener.CacheCommandListener;
import us.smartmc.backend.listener.CommandHandlerListener;
import us.smartmc.backend.listener.MessagingChannelListener;
import us.smartmc.backend.protocol.CacheCommandRequest;
import us.smartmc.backend.protocol.CommandRequest;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

@Getter
public class ConnectionHandler implements Runnable {

    private static boolean defaultListenersAlreadyRegistered;

    @Getter
    protected Socket connection;

    protected final ConnectionOutputStream outputStream;
    protected final ConnectionInputStream inputStream;

    public ConnectionHandler(Socket socket) throws IOException {
        this.connection = socket;
        this.outputStream = new ConnectionOutputStream(this, connection.getOutputStream());
        this.inputStream = new ConnectionInputStream(this, connection.getInputStream());
        registerDefaultListeners();
        registerDefaultCommands();
    }

    private static void registerDefaultCommands() {
        ConnectionInputManager.registerCommands(new ServiceCommand());
    }

    private static void registerDefaultListeners() {
        if (defaultListenersAlreadyRegistered) return;
        ConnectionInputManager
                .registerListeners(
                        new CommandHandlerListener(),
                        new CacheCommandListener(),
                        new MessagingChannelListener());
        defaultListenersAlreadyRegistered = true;
    }

    @Override
    public void run() {
        try {
            while (!connection.isClosed()) {
                Object object = inputStream.readObject();
                if (object instanceof CommandRequest request) {
                    ConnectionInputManager.performCommand(this, request);
                    continue;
                }
                ConnectionInputManager.performListener(this, object);
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void publishMessage(String id, String message) {
        sendObject(new MessageCommand(id, message));
    }

    public void registerCache(String key, Object value) {
        sendObject(new CacheCommandRequest(CacheCommand.build(CacheCommandType.REGISTER, key).value(value)));
    }

    // TODO: HACER METODO PARA OBTENER Y TRABAJAR CON LO OBTENIDO (de momento solo envia instruccion y en segundo plano obtiene valor a local)
    public void getCache(String key, Consumer<Object> consumer) {
        sendObject(new CacheCommandRequest(CacheCommand.build(CacheCommandType.GET, key)));
    }

    public void setCache(String key, Object value) {
        sendObject(new CacheCommandRequest(CacheCommand.build(CacheCommandType.SET, key).value(value)));
    }

    public void removeCache(String key) {
        sendObject(new CacheCommandRequest(CacheCommand.build(CacheCommandType.REMOVE, key)));
    }

    public void handleException(Exception e) {
        try {
            inputStream.close();
            outputStream.close();
            connection.close();
        } catch (IOException ex) {
            handleException(e);
        }
    }

    public void sendCommand(String command) {
        try {
            outputStream.writeCommand(command);
        } catch (IOException e) {
            handleException(e);
        }
    }

    public void sendObject(Object object) {
        try {
            outputStream.writeObject(object);
        } catch (IOException e) {
            handleException(e);
        }
    }
}

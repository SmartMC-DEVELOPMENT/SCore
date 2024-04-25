package us.smartmc.backend.connection;

import lombok.Getter;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.listener.CommandHandlerListener;
import us.smartmc.backend.protocol.*;

import java.io.IOException;
import java.net.Socket;

@Getter
public class ConnectionHandler implements Runnable {

    private static boolean defaultListenersAlreadyRegistered;

    protected final Socket connection;

    protected final ConnectionOutputStream outputStream;
    protected final ConnectionInputStream inputStream;

    public ConnectionHandler(Socket socket) throws IOException {
        this.connection = socket;
        this.outputStream = new ConnectionOutputStream(this, connection.getOutputStream());
        this.inputStream = new ConnectionInputStream(this, connection.getInputStream());
        registerDefaultListeners();
    }

    public ConnectionHandler(Socket socket, ConnectionOutputStream outputStream, ConnectionInputStream inputStream) {
        this.connection = socket;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }

    private static void registerDefaultListeners() {
        if (defaultListenersAlreadyRegistered) return;
        ConnectionInputManager.registerListeners(new CommandHandlerListener());
        defaultListenersAlreadyRegistered = true;
    }

    @Override
    public void run() {
        try {
            while (!connection.isClosed()) {
                ObjectCommand objectCommand = inputStream.readObjectCommand();

                if (objectCommand.getTypeClass().equals(CommandRequest.class)) {
                    CommandRequest request = objectCommand.getObject(CommandRequest.class);
                    ConnectionInputManager.performCommand(this, request);
                    continue;
                }

                ConnectionInputManager.performListener(this, objectCommand.getObject(objectCommand.getTypeClass()));
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleException(Exception e) {
        try {
            inputStream.close();
            outputStream.close();
            connection.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCommand(String command) {
        try {
            outputStream.writeCommand(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendObject(Object object) {
        try {
            outputStream.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

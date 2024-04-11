package us.smartmc.backend.connection;

import lombok.Getter;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.protocol.CommandRequest;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    @Getter
    protected final Socket connection;

    @Getter
    protected final ConnectionOutputStream outputStream;
    @Getter
    protected final ConnectionInputStream inputStream;

    public ConnectionHandler(Socket socket) throws IOException {
        this.connection = socket;
        this.outputStream = new ConnectionOutputStream(this, connection.getOutputStream());
        this.inputStream = new ConnectionInputStream(this, connection.getInputStream());
    }

    public ConnectionHandler(Socket socket, ConnectionOutputStream outputStream, ConnectionInputStream inputStream) {
        this.connection = socket;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        try {
            Object receivedObject = null;
            while ((receivedObject = inputStream.getInputStream().readObject()) != null) {
                System.out.println("Received connectionhandler" + receivedObject);
                if (receivedObject instanceof CommandRequest cmdRequest) {
                    ConnectionInputManager.performCommand(this, cmdRequest);
                } else {
                    ConnectionInputManager.performListener(this, receivedObject);
                }
            }
        } catch (EOFException e) {
            // El flujo de entrada llegó al final, salir del bucle
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void sendMessage(String message) {
        try {
            outputStream.writeUTFMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCommand(String command) {
        sendObject(new CommandRequest(command));
    }

    public void sendObject(Object object) {
        try {
            outputStream.writeObject(object);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

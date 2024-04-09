package us.smartmc.backend.connection;

import lombok.Getter;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.protocol.CommandRequest;
import us.smartmc.backend.protocol.UTFMessage;

import javax.net.ssl.SSLSocket;
import java.io.IOException;

public class ConnectionHandler implements Runnable {

    @Getter
    protected final SSLSocket socket;

    private ConnectionOutputStream outputStream;

    public ConnectionHandler(SSLSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ConnectionInputStream inputStream = new ConnectionInputStream(this);
            outputStream = new ConnectionOutputStream(this);

            Object receivedObject = null;
            while ((receivedObject = inputStream.readObject()) != null) {
                if (receivedObject instanceof CommandRequest cmdRequest) {
                    ConnectionInputManager.performCommand(this, cmdRequest);
                } else {
                    ConnectionInputManager.performListener(this, receivedObject);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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
        sendObject(new CommandRequest(command));
    }

    public void sendObject(Object object) {
        try {
            outputStream.writeObject(object);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

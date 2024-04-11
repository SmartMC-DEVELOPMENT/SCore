package us.smartmc.backend.connection;

import lombok.Getter;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.listener.CommandHandlerListener;
import us.smartmc.backend.protocol.*;

import java.io.IOException;
import java.net.Socket;

@Getter
public class ConnectionHandler implements Runnable {

    protected final Socket connection;

    protected final ConnectionOutputStream outputStream;
    protected final ConnectionInputStream inputStream;

    public ConnectionHandler(Socket socket) throws IOException {
        this.connection = socket;
        this.outputStream = new ConnectionOutputStream(this, connection.getOutputStream());
        this.inputStream = new ConnectionInputStream(this, connection.getInputStream());
        ConnectionInputManager.registerListeners(new CommandHandlerListener());
    }

    public ConnectionHandler(Socket socket, ConnectionOutputStream outputStream, ConnectionInputStream inputStream) {
        this.connection = socket;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        try {
            while (true) {
                byte d = inputStream.readByte();
                DataType type = DataType.getValueOf(d);
                if (type == null) {
                    System.out.println("No type found!");
                    break;
                }
                switch (type) {
                    case UTF -> {
                        String utf = inputStream.readUTF();
                        ConnectionInputManager.performListener(this, utf);
                    }
                    case JSON_OBJECT -> {
                        Object o = inputStream.readJsonObject();
                        if (o instanceof CommandRequest cmdRequest) {
                            ConnectionInputManager.performCommand(this, cmdRequest);
                        } else {
                            ConnectionInputManager.performListener(this, o);
                        }
                    }
                }
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    public void handleException(Exception e) {
        try {
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

    public void sendObjects(Object... args) {
        sendObject(new ObjectsTransfer(args));
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
            System.out.println(System.currentTimeMillis() + "PUT");
            outputStream.writeJsonObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

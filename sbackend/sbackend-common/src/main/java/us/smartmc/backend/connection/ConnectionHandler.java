package us.smartmc.backend.connection;

import lombok.Getter;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.listener.FeedbackObjectListeners;
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
        ConnectionInputManager.registerListeners(new FeedbackObjectListeners());
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
                System.out.println("Received byte " + d);
                DataType type = DataType.getValueOf(d);
                if (type == null) {
                    System.out.println("No type found!");
                    break;
                }
                System.out.println("TYPE=" + type.name());
                switch (type) {
                    case UTF -> {
                        String utf = inputStream.readUTF();
                        System.out.println("Received utf " + utf);
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

                System.out.println(System.currentTimeMillis() + " READ");

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
            System.out.println(System.currentTimeMillis() + "PUT");
            outputStream.writeUTFMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendFeedbackObjectTransfer(FeedbackObjectsRequest request, IFeedbackResponse<?> feedback) {
        sendObject(request.whenComplete(feedback));
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

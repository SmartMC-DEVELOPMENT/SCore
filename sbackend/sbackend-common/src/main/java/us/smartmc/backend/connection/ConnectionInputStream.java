package us.smartmc.backend.connection;

import com.google.gson.Gson;
import lombok.Getter;
import us.smartmc.backend.protocol.ObjectCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

@Getter
public class ConnectionInputStream {

    private final ConnectionHandler connectionHandler;

    private final ObjectInputStream inputStream;

    public ConnectionInputStream(ConnectionHandler connectionHandler, InputStream inputStream) throws IOException {
        this.connectionHandler = connectionHandler;
        this.inputStream = new ObjectInputStream(inputStream);
    }

    public void close() throws IOException {
        inputStream.close();
    }

    public String readUTF() throws IOException {
        return readObject(String.class);
    }

    public ObjectCommand readObjectCommand() throws IOException, ClassNotFoundException {
        return (ObjectCommand) inputStream.readObject();
    }

    public <T> T readObject(Class<? extends T> clazz) throws IOException {
        try {
            Object object = inputStream.readObject();
            if (object instanceof ObjectCommand objectCommand) {
                return objectCommand.getObject(clazz);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new Gson().fromJson(readUTF(), clazz);
    }
}

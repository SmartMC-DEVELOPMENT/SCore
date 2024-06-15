package us.smartmc.backend.connection;

import lombok.Getter;

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

    public String readUTF() throws IOException, ClassNotFoundException {
        return (String) readObject();
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        return inputStream.readObject();
    }
}

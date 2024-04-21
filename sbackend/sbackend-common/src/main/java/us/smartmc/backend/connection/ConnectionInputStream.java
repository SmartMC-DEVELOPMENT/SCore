package us.smartmc.backend.connection;

import com.google.gson.Gson;
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

    public byte readByte() throws IOException {
        return inputStream.readByte();
    }

    public String readUTF() throws IOException {
        return inputStream.readUTF();
    }

    public Object readObject(boolean readByte) throws IOException, ClassNotFoundException {
        if (readByte) inputStream.readByte();
        return inputStream.readObject();
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        return readObject(false);
    }
}

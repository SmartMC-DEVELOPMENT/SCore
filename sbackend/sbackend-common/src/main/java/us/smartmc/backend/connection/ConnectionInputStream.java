package us.smartmc.backend.connection;

import com.google.gson.Gson;
import lombok.Getter;
import us.smartmc.backend.protocol.DataType;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

@Getter
public class ConnectionInputStream {

    private final ConnectionHandler connectionHandler;

    private final DataInputStream inputStream;

    public ConnectionInputStream(ConnectionHandler connectionHandler, InputStream inputStream) {
        this.connectionHandler = connectionHandler;
        this.inputStream = new DataInputStream(inputStream);
    }

    public byte readByte() throws IOException {
        return inputStream.readByte();
    }

    public String readUTF() throws IOException {
        return inputStream.readUTF();
    }

    public Object readJsonObject(boolean readByte) throws IOException, ClassNotFoundException {
        if (readByte) inputStream.readByte();
        String className = inputStream.readUTF();
        String jsonObject = inputStream.readUTF();
        Gson gson = new Gson();
        return gson.fromJson(jsonObject, Class.forName(className));
    }

    public Object readJsonObject() throws IOException, ClassNotFoundException {
        return readJsonObject(false);
    }
}

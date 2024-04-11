package us.smartmc.backend.connection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import lombok.Getter;
import us.smartmc.backend.protocol.UTFMessage;

@Getter
public class ConnectionOutputStream  {

    private final ObjectOutputStream out;
    private final ConnectionHandler connectionHandler;

    public ConnectionOutputStream(ConnectionHandler connectionHandler, OutputStream outputStream) throws IOException {
        this.connectionHandler = connectionHandler;
        this.out = new ObjectOutputStream(outputStream);
    }

    public void writeUTFMessage(String utfMessage) throws IOException {
        writeObject(new UTFMessage(utfMessage));
    }

    public void writeUTF(String utf) throws IOException {
        out.writeUTF(utf);
        flush();
    }

    public void writeObject(Object object) throws IOException {
        out.writeObject(object);
        flush();
        System.out.println("Send object " + object);
    }

    public void flush() throws IOException {
        out.flush();
    }
}

package us.smartmc.backend.connection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import lombok.Getter;
import us.smartmc.backend.protocol.CommandRequest;
import us.smartmc.backend.protocol.DataType;

@Getter
public class ConnectionOutputStream  {

    private final ObjectOutputStream out;
    private final ConnectionHandler connectionHandler;

    public ConnectionOutputStream(ConnectionHandler connectionHandler, OutputStream outputStream) throws IOException {
        this.connectionHandler = connectionHandler;
        this.out = new ObjectOutputStream(outputStream);
    }

    public void close() throws IOException {
        out.close();
    }

    public void writeCommand(String command) throws IOException {
        writeObject(new CommandRequest(command));
    }

    public void writeUTF(String utf) throws IOException {
        send(DataType.UTF, utf);
    }

    public void writeObject(Object object) throws IOException {
        out.writeByte(DataType.OBJECT.getId());
        out.writeObject(object);
        out.flush();
    }

    private void send(DataType type, String utf) throws IOException {
        out.writeByte(type.getId());
        out.writeUTF(utf);
        out.flush();
    }

}

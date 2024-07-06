package us.smartmc.backend.connection;

import lombok.Getter;
import us.smartmc.backend.protocol.CommandRequest;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

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
        send(new CommandRequest(command));
    }

    public void writeObject(Object object) throws IOException {
        send(object);
    }

    private void send(Object o) throws IOException {
        out.writeObject(o);
        out.flush();
    }

}

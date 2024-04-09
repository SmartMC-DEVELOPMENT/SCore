package us.smartmc.backend.connection;

import java.io.IOException;
import java.io.ObjectOutputStream;

import lombok.Getter;
import us.smartmc.backend.protocol.UTFMessage;

@Getter
public class ConnectionOutputStream extends ObjectOutputStream {

    private final ConnectionHandler connectionHandler;

    public ConnectionOutputStream(ConnectionHandler connectionHandler) throws IOException {
        this.connectionHandler = connectionHandler;
    }

    @Override
    public void writeUTF(String utf) throws IOException {
        writeObject(new UTFMessage(utf));
    }

    @Override
    protected void writeObjectOverride(Object arg0) throws IOException {
        super.writeObjectOverride(arg0);
        flush();
    }

}

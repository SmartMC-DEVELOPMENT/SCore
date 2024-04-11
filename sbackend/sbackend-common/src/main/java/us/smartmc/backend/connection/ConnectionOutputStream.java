package us.smartmc.backend.connection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.google.gson.Gson;
import lombok.Getter;
import us.smartmc.backend.protocol.CommandRequest;
import us.smartmc.backend.protocol.DataType;
import us.smartmc.backend.protocol.UTFMessage;

@Getter
public class ConnectionOutputStream  {

    private final DataOutputStream out;
    private final ConnectionHandler connectionHandler;

    public ConnectionOutputStream(ConnectionHandler connectionHandler, OutputStream outputStream) throws IOException {
        this.connectionHandler = connectionHandler;
        this.out = new DataOutputStream(outputStream);
    }

    public void writeCommand(String command) throws IOException {
        writeJsonObject(new CommandRequest(command));
    }

    public void writeUTFMessage(String utfMessage) throws IOException {
        writeJsonObject(new UTFMessage(utfMessage));
    }

    public void writeUTF(String utf) throws IOException {
        send(DataType.UTF, utf);
    }

    public void writeJsonObject(Object object) throws IOException {
        out.writeByte(DataType.JSON_OBJECT.getId());
        out.writeUTF(object.getClass().getName());
        out.writeUTF(new Gson().toJson(object));
        out.flush();
    }

    private void send(DataType type, String utf) throws IOException {
        out.writeByte(type.getId());
        out.writeUTF(utf);
        out.flush();
    }

}

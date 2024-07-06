package us.smartmc.backend.instance.messaging;


import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class MessageCommand implements Serializable {

    private final byte[] id, message;

    public MessageCommand(String id, String message) {
        this.id = id.getBytes(StandardCharsets.UTF_8);
        this.message = message.getBytes(StandardCharsets.UTF_8);
    }

    public String getId() {
        return new String(id, StandardCharsets.UTF_8);
    }

    public String getMessage() {
        return new String(message, StandardCharsets.UTF_8);
    }
}

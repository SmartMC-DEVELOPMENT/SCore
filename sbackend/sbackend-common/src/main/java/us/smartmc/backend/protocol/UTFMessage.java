package us.smartmc.backend.protocol;


import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class UTFMessage implements Serializable {

    private final byte[] message;

    public UTFMessage(String message) {
        this.message = message.getBytes(StandardCharsets.UTF_8);
    }

    public String getMessage() {
        return new String(message, StandardCharsets.UTF_8);
    }
}

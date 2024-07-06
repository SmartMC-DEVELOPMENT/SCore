package us.smartmc.backend.protocol;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class LoginCompleted implements Serializable {

    private final byte[] clientId;

    public LoginCompleted(String clientId) {
        this.clientId = clientId.getBytes(StandardCharsets.UTF_8);
    }

    public String getClientId() {
        return new String(clientId, StandardCharsets.UTF_8);
    }
}

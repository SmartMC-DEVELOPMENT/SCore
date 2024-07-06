package us.smartmc.backend.protocol;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class LoginRequest implements Serializable {

    private final byte[] username, password;

    public LoginRequest(String username, String password) {
        this.username = username.getBytes(StandardCharsets.UTF_8);
        this.password = password.getBytes(StandardCharsets.UTF_8);
    }

    public String getUsername() {
        return new String(username, StandardCharsets.UTF_8);
    }

    public String getPassword() {
        return new String(password, StandardCharsets.UTF_8);
    }
}

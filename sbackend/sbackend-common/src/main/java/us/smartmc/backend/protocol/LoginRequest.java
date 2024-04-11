package us.smartmc.backend.protocol;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class LoginRequest {

    private final String username, password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

}

package us.smartmc.backend.protocol;

import lombok.Getter;

import java.io.Serializable;

public class LoginCompleted implements Serializable {

    @Getter
    private final String clientId;

    public LoginCompleted(String clientId) {
        this.clientId = clientId;
    }

}

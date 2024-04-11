package us.smartmc.backend.protocol;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class UTFMessage {

    private final String message;

    public UTFMessage(String message) {
        this.message = message;
    }

}

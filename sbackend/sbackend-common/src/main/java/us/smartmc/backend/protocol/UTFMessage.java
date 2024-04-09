package us.smartmc.backend.protocol;

import lombok.Getter;

public class UTFMessage {

    @Getter
    private final String message;

    public UTFMessage(String message) {
        this.message = message;
    }
}

package us.smartmc.backend.instance.messaging;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class MessageCommand implements Serializable {

    private final String id, message;

    public MessageCommand(String id, String message) {
        this.id = id;
        this.message = message;
    }

}

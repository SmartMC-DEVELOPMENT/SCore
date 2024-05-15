package us.smartmc.backend.instance.messaging;

import lombok.Getter;

@Getter
public class MessageCommand {

    private final String id, message;

    public MessageCommand(String id, String message) {
        this.id = id;
        this.message = message;
    }

}

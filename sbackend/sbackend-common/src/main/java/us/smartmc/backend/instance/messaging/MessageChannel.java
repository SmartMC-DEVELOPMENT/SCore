package us.smartmc.backend.instance.messaging;

import lombok.Getter;

@Getter
public abstract class MessageChannel implements IMessageChannel {

    private final String id;

    public MessageChannel(String id) {
        this.id = id;
    }

}

package us.smartmc.backend.protocol;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class BroadcastRequest implements Serializable {

    private final Object[] args;
    private final String context;

    public BroadcastRequest(Object... args) {
        this.args = args;
        context = null;
    }

    public BroadcastRequest(String context, Object... args) {
        this.context = context;
        this.args = args;
    }
}

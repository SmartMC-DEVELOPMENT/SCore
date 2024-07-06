package us.smartmc.backend.protocol;

import lombok.Getter;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

@Getter
public class BroadcastRequest implements Serializable {

    private final Object[] args;
    private final byte[] context;

    public BroadcastRequest(Object... args) {
        this.args = args;
        context = null;
    }

    public BroadcastRequest(String context, Object... args) {
        this.context = context.getBytes(StandardCharsets.UTF_8);
        this.args = args;
    }

    public String getContext() {
        return new String(this.context, StandardCharsets.UTF_8);
    }
}

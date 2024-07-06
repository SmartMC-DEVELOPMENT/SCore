package us.smartmc.backend.protocol;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class BroadcastCommandRequest implements Serializable {

    private final byte[] label;
    private final byte[] context;

    public BroadcastCommandRequest(String label) {
        this.label = label.getBytes(StandardCharsets.UTF_8);
        this.context = null;
    }

    public BroadcastCommandRequest(String context, String label) {
        this.context = context.getBytes(StandardCharsets.UTF_8);
        this.label = label.getBytes(StandardCharsets.UTF_8);
    }

    public String getLabel() {
        return new String(label, StandardCharsets.UTF_8);
    }

    public String getContext() {
        return new String(label, StandardCharsets.UTF_8);
    }
}

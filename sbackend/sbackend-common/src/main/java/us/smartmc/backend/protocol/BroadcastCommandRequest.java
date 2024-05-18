package us.smartmc.backend.protocol;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class BroadcastCommandRequest implements Serializable {

    private final String label;
    private final String context;

    public BroadcastCommandRequest(String label) {
        this.label = label;
        this.context = null;
    }

    public BroadcastCommandRequest(String context, String label) {
        this.context = context;
        this.label = label;
    }
}

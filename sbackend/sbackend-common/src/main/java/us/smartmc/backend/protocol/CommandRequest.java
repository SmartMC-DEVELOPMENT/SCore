package us.smartmc.backend.protocol;

import lombok.Getter;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Getter
public class CommandRequest implements Serializable {

    private final byte[] label;

    public CommandRequest(String message) {
        this.label = message.getBytes(StandardCharsets.UTF_8);
    }

    public String[] getArgs() {
        String labelWithoutName = getLabel().replaceFirst(getName() + " ", "");
        return labelWithoutName.split(" ");
    }

    public String getName() {
        String label = getLabel();
        return getLabel().contains(" ") ? label.split(" ")[0] : label;
    }

    public String getLabel() {
        return new String(this.label, StandardCharsets.UTF_8);
    }

}

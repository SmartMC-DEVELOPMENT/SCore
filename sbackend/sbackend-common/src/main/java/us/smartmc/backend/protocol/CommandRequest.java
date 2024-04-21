package us.smartmc.backend.protocol;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class CommandRequest implements Serializable {

    private final String label;

    public CommandRequest(String message) {
        this.label = message;
    }

    public String[] getArgs() {
        String labelWithoutName = label.replaceFirst(getName() + " ", "");
        return labelWithoutName.split(" ");
    }

    public String getName() {
        return label.contains(" ") ? label.split(" ")[0] : label;
    }
}

package us.smartmc.serverhandler.instance;

import java.io.Serializable;

public class BackendCommandExecuteRequest implements Serializable {

    private final String label;

    public BackendCommandExecuteRequest(String cmdLine) {
        this.label = cmdLine;
    }

    public String getName() {
        return label.split(" ")[0];
    }

    public String[] getArgs() {
        return label.replaceFirst(getName() + " ", "").split(" ");
    }

    public String getLabel() {
        return label;
    }
}

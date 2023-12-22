package us.smartmc.serverhandler.executor;

public abstract class BackendCommand implements IBackendCommand {

    private final String name;

    public BackendCommand(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}

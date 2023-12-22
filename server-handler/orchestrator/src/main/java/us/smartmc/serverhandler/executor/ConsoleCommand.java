package us.smartmc.serverhandler.executor;

public abstract class ConsoleCommand implements IConsoleCommand {

    protected final ConsoleCommandInfo info;

    public ConsoleCommand() {
        info = getClass().getDeclaredAnnotation(ConsoleCommandInfo.class);
    }

    @Override
    public String getName() {
        return info.name();
    }

    @Override
    public String getDescription() {
        return info.description();
    }
}

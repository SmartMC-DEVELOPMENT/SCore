package us.smartmc.serverhandler.executor;

public abstract class ConsoleCommand implements IConsoleCommand {

    protected final ConsoleCommandInfo info = this.getClass().getDeclaredAnnotation(ConsoleCommandInfo.class);

    @Override
    public String getName() {
        return this.info.name();
    }

    @Override
    public String getDescription() {
        return this.info.description();
    }

    @Override
    public int getMinArgs() {
        return this.info.minArgs();
    }

    @Override
    public int getMaxArgs() {
        return this.info.maxArgs();
    }

    @Override
    public String getUsage() {
        return this.info.usage();
    }

    @Override
    public String[] getAliases() {
        return this.info.aliases();
    }
}

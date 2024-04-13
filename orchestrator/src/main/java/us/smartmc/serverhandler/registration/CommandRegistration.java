package us.smartmc.serverhandler.registration;

import us.smartmc.serverhandler.IRegistration;
import us.smartmc.serverhandler.backendcommand.IAmAProxyCommand;
import us.smartmc.serverhandler.backendcommand.RegisterServerCommand;
import us.smartmc.serverhandler.backendcommand.ServerStatusCommand;
import us.smartmc.serverhandler.consolecommand.*;
import us.smartmc.serverhandler.manager.BackendCommandManager;
import us.smartmc.serverhandler.manager.ConsoleCommandManager;

public class CommandRegistration implements IRegistration {

    @Override
    public void register() {
        ConsoleCommandManager.register(
                new HelpCommand(),
                new TestCommand(),
                new ExitCommand(),
                new CreateCommand(),
                new DeleteCommand(),
                new ExecuteCommand(),
                new RestartCommand(),
                new StopCommand());

        BackendCommandManager.register(
                new ServerStatusCommand(),
                new RegisterServerCommand(),
                new IAmAProxyCommand());
    }
}

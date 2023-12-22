package us.smartmc.serverhandler.registration;

import us.smartmc.serverhandler.backendcommand.IAmAProxyCommand;
import us.smartmc.serverhandler.backendcommand.RegisterServerCommand;
import us.smartmc.serverhandler.backendcommand.ServerStatusCommand;
import us.smartmc.serverhandler.consolecommand.CreateCommand;
import us.smartmc.serverhandler.consolecommand.HelpCommand;
import us.smartmc.serverhandler.consolecommand.ExitCommand;
import us.smartmc.serverhandler.consolecommand.TestCommand;
import us.smartmc.serverhandler.manager.BackendCommandManager;
import us.smartmc.serverhandler.manager.ConsoleCommandManager;
import us.smartmc.serverhandler.IRegistration;

public class CommandRegistration implements IRegistration {

    @Override
    public void register() {
        ConsoleCommandManager.register(
                new HelpCommand(),
                new TestCommand(),
                new CreateCommand(),
                new ExitCommand());

        BackendCommandManager.register(new ServerStatusCommand(),
                new RegisterServerCommand(), new IAmAProxyCommand());
    }
}

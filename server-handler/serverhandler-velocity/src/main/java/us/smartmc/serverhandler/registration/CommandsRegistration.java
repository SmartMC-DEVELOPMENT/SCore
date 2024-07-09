package us.smartmc.serverhandler.registration;

import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.serverhandler.IRegistration;
import us.smartmc.serverhandler.backendcommand.RegisterServerCommand;
import us.smartmc.serverhandler.backendcommand.RegisterServersCommand;
import us.smartmc.serverhandler.backendcommand.UnregisterServerCommand;
import us.smartmc.serverhandler.manager.BackendCommandManager;

public class CommandsRegistration implements IRegistration {

    @Override
    public void register() {
        ConnectionInputManager.registerCommands(
                new RegisterServerCommand(),
                new RegisterServersCommand(),
                new UnregisterServerCommand());
    }
}

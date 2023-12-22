package us.smartmc.serverhandler.registration;

import us.smartmc.serverhandler.IRegistration;
import us.smartmc.serverhandler.backendcommand.RegisterServerCommand;
import us.smartmc.serverhandler.backendcommand.RegisterServersCommand;
import us.smartmc.serverhandler.backendcommand.UnregisterServerCommand;
import us.smartmc.serverhandler.manager.BackendCommandManager;

public class CommandsRegistration implements IRegistration {

    @Override
    public void register() {
        BackendCommandManager.register(
                new RegisterServerCommand(),
                new RegisterServersCommand(),
                new UnregisterServerCommand());
    }
}

package us.smartmc.serverhandler.registration;

import us.smartmc.serverhandler.IRegistration;
import us.smartmc.serverhandler.command.ExecuteServerMessageCommand;
import us.smartmc.serverhandler.manager.BackendCommandManager;

public class CommandRegistration implements IRegistration {
  @Override
  public void register() {
    BackendCommandManager.register(
            new ExecuteServerMessageCommand()
    );
  }
}


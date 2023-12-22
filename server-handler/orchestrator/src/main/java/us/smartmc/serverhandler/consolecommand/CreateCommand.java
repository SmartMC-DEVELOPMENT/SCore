package us.smartmc.serverhandler.consolecommand;

import us.smartmc.serverhandler.executor.ConsoleCommandInfo;
import us.smartmc.serverhandler.executor.ConsoleCommand;
import us.smartmc.serverhandler.manager.ServerManager;

@ConsoleCommandInfo(
        name = "create",
        description = "Create determinate server by configuration name"
)
public class CreateCommand extends ConsoleCommand {

    @Override
    public void execute(String label, String[] args) {
        ServerManager.create(args[0]);
    }
}

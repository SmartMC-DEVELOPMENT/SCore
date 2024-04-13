package us.smartmc.serverhandler.consolecommand;

import us.smartmc.serverhandler.executor.ConsoleCommand;
import us.smartmc.serverhandler.executor.ConsoleCommandInfo;
import us.smartmc.serverhandler.manager.ServerManager;
import us.smartmc.serverhandler.util.CommandUtilities;

@ConsoleCommandInfo(
        name = "create",
        description = "Create determinate server by configuration name",
        usage = "/create <serverName> [amount]",
        minArgs = 1,
        maxArgs = 2
)
public class CreateCommand extends ConsoleCommand {

    @Override
    public void execute(String label, String[] args) {
        final String name = args[0];

        final int amount = args.length == 2 ? Integer.parseInt(args[1]) : 1;
        for (int i = 0; i < amount; i++) {
            final String serverNumber = amount == 1 ? "" : " number " + (i + 1);

            CommandUtilities.sendFeedback("Creating server %s%s...", name, serverNumber);
            ServerManager.create(name);
        }
    }
}

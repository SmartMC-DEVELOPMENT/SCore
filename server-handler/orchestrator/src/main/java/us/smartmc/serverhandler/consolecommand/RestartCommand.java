package us.smartmc.serverhandler.consolecommand;

import us.smartmc.serverhandler.executor.ConsoleCommand;
import us.smartmc.serverhandler.executor.ConsoleCommandInfo;
import us.smartmc.serverhandler.manager.BackendProxyConnectionHandler;
import us.smartmc.serverhandler.manager.ServerManager;
import us.smartmc.serverhandler.util.CommandUtilities;

import java.util.Timer;
import java.util.TimerTask;

@ConsoleCommandInfo(
        name = "restart",
        description = "Delete and create X server by name",
        usage = "/restart <serverName>",
        minArgs = 1,
        maxArgs = 1
)
public class RestartCommand extends ConsoleCommand {

    @Override
    public void execute(String label, String[] args) {
        final String name = args[0];

        boolean exists = ServerManager.exists(name);
        if (!exists) {
            CommandUtilities.sendError("Server with name %s not found.", name);
            return;
        }

        DeleteCommand.deleteServer(name);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                String newName;
                newName = name.replaceAll("[0-9]", "");
                if (newName.endsWith("-")) newName = newName.substring(0, newName.length() - 1);

                CommandUtilities.sendFeedback("Creating server %s...", newName);
                ServerManager.create(newName);
            }
        }, 750L);
    }
}
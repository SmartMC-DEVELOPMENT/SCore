package us.smartmc.serverhandler.consolecommand;

import us.smartmc.serverhandler.executor.ConsoleCommand;
import us.smartmc.serverhandler.executor.ConsoleCommandInfo;
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

        final boolean status = DeleteCommand.deleteServer(name);
        if (!status) {
            CommandUtilities.sendError("Server with name %s not found.", name);
            return;
        }
        CommandUtilities.sendFeedback("Server %s has been deleted.", name);

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
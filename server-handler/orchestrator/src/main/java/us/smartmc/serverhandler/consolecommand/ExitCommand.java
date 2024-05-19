package us.smartmc.serverhandler.consolecommand;

import us.smartmc.serverhandler.executor.ConsoleCommand;
import us.smartmc.serverhandler.executor.ConsoleCommandInfo;

@ConsoleCommandInfo(
        name = "bye",
        aliases = {"exit", "quit", "close", "adios", "cerrar", "dejame"},
        description = "Exit from console correctly to make his things",
        maxArgs = 0
)
public class ExitCommand extends ConsoleCommand {

    @Override
    public void execute(String label, String[] args) {
        System.exit(0);
    }
}

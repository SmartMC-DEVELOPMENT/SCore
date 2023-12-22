package us.smartmc.serverhandler.consolecommand;

import us.smartmc.serverhandler.executor.ConsoleCommandInfo;
import us.smartmc.serverhandler.executor.ConsoleCommand;
import us.smartmc.serverhandler.manager.ConsoleCommandManager;

@ConsoleCommandInfo(
        name = "help",
        description = "Show all available commands"
)
public class HelpCommand extends ConsoleCommand {

    @Override
    public void execute(String label, String[] args) {
        System.out.println(ConsoleCommandManager.getCommandInfo());
    }
}

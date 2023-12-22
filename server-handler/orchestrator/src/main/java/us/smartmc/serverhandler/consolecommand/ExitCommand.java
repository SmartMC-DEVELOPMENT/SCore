package us.smartmc.serverhandler.consolecommand;

import us.smartmc.serverhandler.executor.ConsoleCommand;
import us.smartmc.serverhandler.executor.ConsoleCommandInfo;

@ConsoleCommandInfo(
        name = "exit",
        description = "Exit from console correctly to make his things"
)
public class ExitCommand extends ConsoleCommand {

    @Override
    public void execute(String label, String[] args) {
        System.exit(0);
    }

    public static void execute() {
        System.out.println("\nGoodbye! ;D");
    }
}

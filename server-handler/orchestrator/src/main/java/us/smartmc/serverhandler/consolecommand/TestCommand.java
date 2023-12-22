package us.smartmc.serverhandler.consolecommand;

import us.smartmc.serverhandler.executor.ConsoleCommand;
import us.smartmc.serverhandler.executor.ConsoleCommandInfo;

@ConsoleCommandInfo(
        name = "test",
        description = "Test command that would be never deleted"
)
public class TestCommand extends ConsoleCommand {

    @Override
    public void execute(String label, String[] args) {
        System.out.println("This is an test! Hello world!");
    }
}

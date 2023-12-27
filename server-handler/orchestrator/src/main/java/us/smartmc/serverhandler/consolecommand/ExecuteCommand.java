package us.smartmc.serverhandler.consolecommand;

import us.smartmc.serverhandler.executor.ConsoleCommand;
import us.smartmc.serverhandler.executor.ConsoleCommandInfo;
import us.smartmc.serverhandler.instance.ServerInfo;
import us.smartmc.serverhandler.manager.ServerManager;
import us.smartmc.serverhandler.util.CommandUtilities;

@ConsoleCommandInfo(
        name = "execute",
        description = "Execute command on a registered server or selected screen.",
        usage = "/execute server/screen <serverName/screenName> <command>",
        minArgs = 3
)
public class ExecuteCommand extends ConsoleCommand {

  @Override
  public void execute(String label, String[] args) {
    final ExecuteType type = ExecuteType.getExecuteType(args[0]);
    if (type == null) {
      CommandUtilities.sendError("You have introduced an invalid executor type: '%s'", args[0]);
      return;
    }

    final String name = args[1];

    final String[] commandArgs = new String[args.length - 2];
    System.arraycopy(args, 2, commandArgs, 0, commandArgs.length);
    final String command = String.join(" ", commandArgs);

    String screenName = name;

    if (type == ExecuteType.SERVER) {
      final ServerInfo serverInfo = ServerManager.get(name);
      if (serverInfo == null) {
        CommandUtilities.sendError("Server '%s' not found!", name);
        return;
      }

      screenName = serverInfo.getDirectory().getName();
    }

    if (!ServerManager.sendConsoleMessage(screenName, command).join()) {
      CommandUtilities.sendError("Something went wrong trying to execute the command.");
      return;
    }
    CommandUtilities.sendFeedback("Command executed successfully in: '%s'", name);
  }

  protected enum ExecuteType {
    SERVER,
    SCREEN;

    public static ExecuteType getExecuteType(String value) {
      for (ExecuteType type : ExecuteType.values()) {
        if (type.name().equalsIgnoreCase(value)) {
          return type;
        }
      }
      return null;
    }
  }
}

package us.smartmc.serverhandler.consolecommand;

import us.smartmc.serverhandler.executor.ConsoleCommand;
import us.smartmc.serverhandler.executor.ConsoleCommandInfo;
import us.smartmc.serverhandler.instance.ServerInfo;
import us.smartmc.serverhandler.manager.ServerManager;
import us.smartmc.serverhandler.util.CommandUtilities;

@ConsoleCommandInfo(
        name = "delete",
        description = "Stop and delete determinate server by name",
        usage = "/delete <serverName>",
        minArgs = 1,
        maxArgs = 1
)
public class DeleteCommand extends ConsoleCommand {

  @Override
  public void execute(String label, String[] args) {
    final String name = args[0];

    final boolean status = deleteServer(name);
    if (!status) {
      CommandUtilities.sendError("Server with name %s not found.", name);
      return;
    }
    CommandUtilities.sendFeedback("Server %s has been deleted.", name);
  }

  protected static boolean deleteServer(String serverName) {
    final ServerInfo serverInfo = ServerManager.get(serverName);
    if (serverInfo == null) {
      return false;
    }

    return ServerManager.stopServer(serverInfo);
  }
}

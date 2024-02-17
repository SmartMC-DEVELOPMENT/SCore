package us.smartmc.serverhandler.consolecommand;

import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.instance.builder.DiscordLogEmbedBuilder;
import us.smartmc.serverhandler.executor.ConsoleCommand;
import us.smartmc.serverhandler.executor.ConsoleCommandInfo;
import us.smartmc.serverhandler.instance.ServerInfo;
import us.smartmc.serverhandler.manager.BackendProxyConnectionHandler;
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

    final boolean status = ServerManager.exists(name);
    if (!status) {
      CommandUtilities.sendError("Server with name %s not found.", name);
      return;
    }
    deleteServer(name);
  }

  public static void deleteServer(String serverName) {
    new Thread(() -> {
      final ServerInfo serverInfo = ServerManager.get(serverName);
      if (serverInfo == null) return;

      BackendProxyConnectionHandler.broadcast("unregisterServer " + serverName);
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      ServerManager.stopServer(serverInfo);
      CommandUtilities.sendFeedback("Server %s has been deleted.", serverName);
      new DiscordLogEmbedBuilder()
              .title("Servidor eliminado!").description("Se ha elimina un servidor anteriormente conectado a un ServerHandler")
              .addField("Nombre", serverInfo.getName())
              .addField("IP", serverInfo.getHostname(), true)
              .addField("Puerto", String.valueOf(serverInfo.getPort()), true)
              .addField("Directorio", serverInfo.getDirectory().getAbsolutePath())
              .color("RED").send(RedisConnection.mainConnection.getResource());
    }).start();
  }

}

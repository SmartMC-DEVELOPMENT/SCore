package us.smartmc.serverhandler.backendcommand;

import me.imsergioh.jbackend.api.ConnectionHandler;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.instance.builder.DiscordLogEmbedBuilder;
import us.smartmc.serverhandler.executor.BackendCommand;
import us.smartmc.serverhandler.instance.CommonServerInfo;
import us.smartmc.serverhandler.instance.ServerStatus;
import us.smartmc.serverhandler.manager.CacheCleanerManager;
import us.smartmc.serverhandler.manager.ServerManager;

public class ServerStatusCommand extends BackendCommand {

    public ServerStatusCommand() {
        super("serverStatus");
    }

    // EXAMPLE: serverStatus <active, idle...> <name>

    @Override
    public void execute(ConnectionHandler handler, String label, String[] args) {
        if(executeStatus(args, "active", ServerStatus.ACTIVE)) {
            CommonServerInfo serverInfo = ServerManager.get(args[1]);
            System.out.println("New server connected -> " + serverInfo.getName() + "!");

            new DiscordLogEmbedBuilder()
                    .title("Nuevo servidor conectado!").description("Se ha conectado un nuevo servidor correctamente a ServerHandler")
                    .addField("Nombre", serverInfo.getName())
                    .addField("IP", "||" + serverInfo.getHostname() + "||", true)
                    .addField("Puerto", String.valueOf(serverInfo.getPort()), true)
                    .color("GREEN").send(RedisConnection.mainConnection.getResource());
            return;
        }

        if (executeStatus(args, "idle", ServerStatus.IDLE)) {
            String name = args[1];
            CommonServerInfo serverInfo = ServerManager.get(args[1]);
            ServerManager.deleteIfNotTemporalAndUnregister(name);
            new DiscordLogEmbedBuilder()
                    .title("Servidor desconectado!").description("Se ha desconectado un nuevo servidor correctamente de ServerHandler")
                    .addField("Nombre", serverInfo.getName())
                    .addField("IP", "||" + serverInfo.getHostname() + "||", true)
                    .addField("Puerto", String.valueOf(serverInfo.getPort()), true)
                    .color("RED").send(RedisConnection.mainConnection.getResource());
            CacheCleanerManager.removeServerCache(name);
        }
    }

    private boolean executeStatus(String[] args, String argCmd, ServerStatus status) {
        if (args[0].equals(argCmd)) {
            String name = args[1];
            CommonServerInfo serverInfo = ServerManager.get(name);
            serverInfo.setStatus(status);
            return true;
        }
        return false;
    }
}

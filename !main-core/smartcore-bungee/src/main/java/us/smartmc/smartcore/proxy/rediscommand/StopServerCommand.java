package us.smartmc.smartcore.proxy.rediscommand;

import me.imsergioh.pluginsapi.instance.handler.RedisPubSubListener;
import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import us.smartmc.smartcore.proxy.manager.ServersHandler;

import java.util.List;
import java.util.Random;

public class StopServerCommand extends RedisPubSubListener {

    public StopServerCommand() {
        super("server:restart");
    }

    @Override
    public void onMessage(String message) {
        String[] args = message.split("@");
        String bukkitServerId = args[0];
        int port = Integer.parseInt(args[1]);
        ServerInfo server = getServerByPort(port);
        if (server == null) {
            System.out.println("Unknown server is being restarted! (" + port + ")");
            return;
        }
        sendToLobbyServer(server);
    }

    public void sendToLobbyServer(ServerInfo server) {
        List<String> lobbies = ServersHandler.getLobbyServers("lobby");

        for (ProxiedPlayer player : BungeeCordPluginsAPI.proxy.getServerInfo(server.getName()).getPlayers()) {
            String serverNameToSend = lobbies.get(new Random().nextInt(lobbies.size()));
            ServerInfo serverToSend = BungeeCordPluginsAPI.proxy.getServerInfo(serverNameToSend);
            player.connect(serverToSend);
        }
    }

    public ServerInfo getServerByPort(int port) {
        for (ServerInfo server : BungeeCordPluginsAPI.proxy.getServers().values()) {
            if (server.getAddress().getPort() == port) return server;
        }
        return null;
    }

}

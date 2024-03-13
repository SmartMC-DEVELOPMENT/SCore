package us.smartmc.smartcore.smartcorevelocity.rediscommand;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import me.imsergioh.pluginsapi.instance.handler.RedisPubSubListener;
import us.smartmc.smartcore.smartcorevelocity.manager.ServersHandler;
import us.smartmc.smartcore.velocitycore.manager.VelocityPluginsAPI;

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

        for (Player player : VelocityPluginsAPI.proxy.getServer(server.getName()).get().getPlayersConnected()) {
            String serverNameToSend = lobbies.get(new Random().nextInt(lobbies.size()));
            RegisteredServer serverToSend = VelocityPluginsAPI.proxy.getServer(serverNameToSend).get();
            player.createConnectionRequest(serverToSend).fireAndForget();
        }
    }

    public ServerInfo getServerByPort(int port) {
        for (RegisteredServer server : VelocityPluginsAPI.proxy.getAllServers()) {
            if (server.getServerInfo().getAddress().getPort() == port) return server.getServerInfo();
        }
        return null;
    }

}

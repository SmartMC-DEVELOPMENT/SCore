package me.imsergioh.smartcorewaterfall.rediscommand;

import me.imsergioh.smartcorewaterfall.manager.ServersHandler;
import us.smartmc.core.pluginsapi.instance.handler.RedisPubSubListener;
import me.imsergioh.smartcorewaterfall.SmartCoreWaterfall;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;

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
        List<String> lobbies = ServersHandler.getLobbyServers("main-lobby-");

        for (ProxiedPlayer player : server.getPlayers()) {
            String serverNameToSend = lobbies.get(new Random().nextInt(lobbies.size()));
            ServerInfo serverToSend = SmartCoreWaterfall.getPlugin().getProxy().getServerInfo(serverNameToSend);
            player.connect(serverToSend, ServerConnectEvent.Reason.KICK_REDIRECT);
        }
    }

    public ServerInfo getServerByPort(int port) {
        for (ServerInfo serverInfo : SmartCoreWaterfall.getPlugin().getProxy().getServersCopy().values()) {
            if (serverInfo.getAddress().getPort() == port) return serverInfo;
        }
        return null;
    }

}

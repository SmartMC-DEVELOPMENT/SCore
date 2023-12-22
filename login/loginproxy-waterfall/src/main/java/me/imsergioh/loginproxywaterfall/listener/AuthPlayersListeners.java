package me.imsergioh.loginproxywaterfall.listener;

import me.imsergioh.loginproxywaterfall.LoginProxyWaterfall;
import me.imsergioh.loginproxywaterfall.instance.LoginPlayer;
import me.imsergioh.loginproxywaterfall.manager.LoginPlayersFactory;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AuthPlayersListeners implements Listener {

    @EventHandler(priority = 1)
    public void connect(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        LoginPlayer loginPlayer = LoginPlayersFactory.get(player);
        if (loginPlayer == null) return;

        // CHECK FOR EXECUTING ONLY ONCE
        if (loginPlayer.isCheck()) return;
        loginPlayer.setCheck(true);

        // IS PREMIUM USER = SET AUTH AUTO TO TRUE
        if (player.getUniqueId().version() == 4) {
            loginPlayer.setAuth(true);
        }

        if (!loginPlayer.isPremium()) {
            // CRACKED
            loginPlayer.checkSecretKey();
        } else {
            // PREMIUM
            List<String> servers = LoginProxyWaterfall.getPlugin().getServers("lobby");
            connectTo(player, servers);
        }
    }

    public static void connectTo(ProxiedPlayer player, List<String> serverList) {
        String randomServer = serverList.get(new Random().nextInt(serverList.size()));
        ServerInfo serverInfo = LoginProxyWaterfall.getPlugin().getProxy().getServerInfo(randomServer);
        if (player.getServer() != null &&
                player.getServer().getInfo() != null &&
                player.getServer().getInfo().getName().equals(serverInfo.getName())) return;
        player.connect(serverInfo, ServerConnectEvent.Reason.JOIN_PROXY);
    }

}

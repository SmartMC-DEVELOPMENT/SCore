package me.imsergioh.loginproxywaterfall.listener;

import me.imsergioh.loginproxywaterfall.manager.LoginPlayersFactory;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginPlayersFactoryListeners implements Listener {

    @EventHandler
    public void connect(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        LoginPlayersFactory.load(player);
        System.out.println("Connecting " + event.getPlayer().getName() + " to " + event.getTarget().getName());
    }

    @EventHandler(priority = 64)
    public void disconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        LoginPlayersFactory.unload(player.getUniqueId());
    }
}

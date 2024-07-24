package us.smartmc.smartcore.proxy.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import us.smartmc.smartcore.proxy.instance.player.SmartCoreProxyPlayer;

public class BackendEssentialListeners implements Listener {

    @EventHandler
    public void onConnect(ServerConnectedEvent event) {
        if (SmartCoreProxyPlayer.alreadyRegistered(event.getPlayer())) return;

        SmartCoreProxyPlayer.get(event.getPlayer()).load();
    }

    @EventHandler
    public void onConnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (player.isConnected()) return;

        SmartCoreProxyPlayer.get(event.getPlayer()).unload();
    }
}

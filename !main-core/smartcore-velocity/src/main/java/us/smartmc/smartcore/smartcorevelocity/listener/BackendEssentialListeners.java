package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import us.smartmc.smartcore.smartcorevelocity.instance.player.SmartCoreProxyPlayer;

public class BackendEssentialListeners {

    @Subscribe
    public void onConnect(ServerConnectedEvent event) {
        if (SmartCoreProxyPlayer.alreadyRegistered(event.getPlayer())) return;

        SmartCoreProxyPlayer.get(event.getPlayer()).load();
    }

    @Subscribe
    public void onConnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        if (player.isActive()) return;

        SmartCoreProxyPlayer.get(event.getPlayer()).unload();
    }
}

package us.smartmc.serverhandler.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import us.smartmc.serverhandler.request.ServerUnregisterRequest;

public class RequestsListeners implements Listener {

    @EventHandler
    public void onConnected(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ServerUnregisterRequest.requests.forEach(request -> {
            request.getConnecting().remove(player.getUniqueId());
        });
    }
}

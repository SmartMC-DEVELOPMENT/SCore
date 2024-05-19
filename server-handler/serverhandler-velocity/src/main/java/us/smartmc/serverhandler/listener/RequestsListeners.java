package us.smartmc.serverhandler.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import us.smartmc.serverhandler.request.ServerUnregisterRequest;

public class RequestsListeners {

    @Subscribe
    public void onConnected(ServerConnectedEvent event) {
        Player player = event.getPlayer();
        ServerUnregisterRequest.requests.forEach(request -> {
            request.getConnecting().remove(player.getUniqueId());
        });
    }
}

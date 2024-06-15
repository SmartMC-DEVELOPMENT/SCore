package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import us.smartmc.backend.handler.ServicesManager;
import us.smartmc.smartcore.smartcorevelocity.backend.service.PlayersService;

public class BackendEssentialListeners {

    @Subscribe
    public void onConnect(ServerConnectedEvent event) {
        ServicesManager.get(PlayersService.class).registerPlayerContext(event.getPlayer());
    }

    @Subscribe
    public void onConnect(DisconnectEvent event) {
        ServicesManager.get(PlayersService.class).unregisterPlayerContext(event.getPlayer());
    }
}

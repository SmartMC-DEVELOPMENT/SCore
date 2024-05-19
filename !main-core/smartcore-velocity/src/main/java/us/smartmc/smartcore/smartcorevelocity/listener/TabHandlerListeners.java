package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import us.smartmc.smartcore.smartcorevelocity.instance.PlayerLanguages;
import us.smartmc.smartcore.smartcorevelocity.manager.TabHandler;

public class TabHandlerListeners {

    @Subscribe
    public void send(ServerConnectedEvent event) {
        TabHandler handler =  TabHandler.getHandlers().get(PlayerLanguages.getLanguage(event.getPlayer()));
        handler.sendTab(event.getPlayer(), handler.getCurrentHeader(), handler.getCurrentFooter());
    }

}

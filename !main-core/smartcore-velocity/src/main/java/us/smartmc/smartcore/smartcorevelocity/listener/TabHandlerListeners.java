package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import us.smartmc.smartcore.smartcorevelocity.instance.PlayerLanguages;
import us.smartmc.smartcore.smartcorevelocity.manager.TabHandler;

import java.util.Timer;
import java.util.TimerTask;

public class TabHandlerListeners {

    @Subscribe
    public void send(ServerConnectedEvent event) {
        Player player = event.getPlayer();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (player == null) return;
                if (!player.isActive()) return;
                TabHandler handler = TabHandler.getHandlers().get(PlayerLanguages.getLanguage(event.getPlayer()));
                handler.sendTab(event.getPlayer(), handler.getCurrentHeader(), handler.getCurrentFooter());
            }
        }, 75L);
    }

}

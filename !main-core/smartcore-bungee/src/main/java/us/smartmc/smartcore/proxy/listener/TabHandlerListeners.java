package us.smartmc.smartcore.proxy.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import us.smartmc.smartcore.proxy.instance.PlayerLanguages;
import us.smartmc.smartcore.proxy.manager.TabHandler;

import java.util.Timer;
import java.util.TimerTask;

public class TabHandlerListeners implements Listener {

    @EventHandler
    public void send(ServerConnectedEvent event) {
        ProxiedPlayer player = event.getPlayer();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (player == null) return;
                if (!player.isConnected()) return;
                TabHandler handler = TabHandler.getHandlers().get(PlayerLanguages.getLanguage(event.getPlayer()));
                handler.sendTab(event.getPlayer(), handler.getCurrentHeader(), handler.getCurrentFooter());
            }
        }, 75L);
    }

}

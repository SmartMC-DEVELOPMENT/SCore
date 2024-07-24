package us.smartmc.smartcore.proxy.listener;

import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import us.smartmc.smartcore.proxy.instance.PlayerLanguages;

public class LanguageListeners implements Listener {

    @EventHandler
    public void onConnect(ServerConnectedEvent event) {
        PlayerLanguages.getLanguage(event.getPlayer());
        System.out.println(PlayerLanguages.getLanguage(event.getPlayer()).name());
    }
}

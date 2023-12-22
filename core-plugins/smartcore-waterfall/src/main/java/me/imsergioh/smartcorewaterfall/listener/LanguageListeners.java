package me.imsergioh.smartcorewaterfall.listener;

import me.imsergioh.smartcorewaterfall.instance.PlayerLanguages;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LanguageListeners implements Listener {

    @EventHandler
    public void onConnect(ServerConnectedEvent event) {
        PlayerLanguages.getLanguage(event.getPlayer());
        System.out.println(PlayerLanguages.getLanguage(event.getPlayer()).name());
    }
}

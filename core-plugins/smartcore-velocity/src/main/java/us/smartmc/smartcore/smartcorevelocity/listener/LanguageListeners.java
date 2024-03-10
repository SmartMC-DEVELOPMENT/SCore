package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import us.smartmc.smartcore.smartcorevelocity.instance.PlayerLanguages;

public class LanguageListeners {

    @Subscribe
    public void onConnect(ServerConnectedEvent event) {
        PlayerLanguages.getLanguage(event.getPlayer());
        System.out.println(PlayerLanguages.getLanguage(event.getPlayer()).name());
    }
}

package us.smartmc.tonterias.listener;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import us.smartmc.smartaddons.plugin.AddonListener;
import us.smartmc.tonterias.manager.WhitelistManager;

public class WhitelistListener extends AddonListener implements Listener {

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        if (!isEnabled()) return;
        String name = event.getName().toLowerCase();

        if (WhitelistManager.getWhitelist().contains(name)) {
            return;
        }

        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST,
                Component.text("No estás en el evento. ¿Cual? El de mi polla al viento"));
    }

}

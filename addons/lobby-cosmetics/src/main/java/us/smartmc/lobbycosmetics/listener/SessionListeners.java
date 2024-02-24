package us.smartmc.lobbycosmetics.listener;

import me.imsergioh.pluginsapi.event.PlayerUnloadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.smartmc.lobbycosmetics.LobbyCosmetics;
import us.smartmc.lobbycosmetics.handler.CosmeticSessionHandler;
import us.smartmc.smartaddons.plugin.AddonListener;

public class SessionListeners extends AddonListener implements Listener {

    private static final CosmeticSessionHandler handler = LobbyCosmetics.getPlayerSessionsHandler();

    @EventHandler
    public void unloadSession(PlayerUnloadEvent event) {
        if (!isEnabled()) return;
        handler.unregister(event.getCorePlayer().getUUID()).unload();
    }
}

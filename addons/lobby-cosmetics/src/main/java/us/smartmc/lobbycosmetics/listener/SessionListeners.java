package us.smartmc.lobbycosmetics.listener;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.event.PlayerUnloadEvent;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import us.smartmc.lobbycosmetics.LobbyCosmetics;
import us.smartmc.lobbycosmetics.handler.CosmeticSessionHandler;
import us.smartmc.lobbycosmetics.instance.player.CosmeticPlayerSession;
import us.smartmc.smartaddons.plugin.AddonListener;

public class SessionListeners extends AddonListener implements Listener {

    private static final CosmeticSessionHandler handler = LobbyCosmetics.getPlayerSessionsHandler();

    @EventHandler
    public void unloadSession(PlayerUnloadEvent event) {
        if (!isEnabled()) return;
        CosmeticPlayerSession session = handler.unregister(event.getPlayer().getUniqueId());
        if (session != null) session.unload();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSergiohJoinTest(PlayerDataLoadedEvent event) {
        if (!isEnabled()) return;
        CorePlayer corePlayer = event.getCorePlayer();
        if (!corePlayer.get().getName().equals("ImSergioh")) return;
        handler.get(corePlayer.getUUID());
    }
}

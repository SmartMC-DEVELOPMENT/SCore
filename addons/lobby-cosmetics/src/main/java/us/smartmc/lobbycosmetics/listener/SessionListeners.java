package us.smartmc.lobbycosmetics.listener;

import me.imsergioh.pluginsapi.event.PlayerUnloadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.smartmc.lobbycosmetics.instance.CosmeticPlayerSession;
import us.smartmc.smartaddons.plugin.AddonListener;

public class SessionListeners extends AddonListener implements Listener {

    @EventHandler
    public void unloadSession(PlayerUnloadEvent event) {
        if (!isEnabled()) return;
        CosmeticPlayerSession.remove(event.getCorePlayer().getUUID());
    }
}

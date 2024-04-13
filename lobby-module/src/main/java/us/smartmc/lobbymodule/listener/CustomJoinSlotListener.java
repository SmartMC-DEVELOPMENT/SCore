package us.smartmc.lobbymodule.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import us.smartmc.lobbymodule.LobbyModule;
import us.smartmc.smartaddons.plugin.AddonListener;

public class CustomJoinSlotListener extends AddonListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!isEnabled()) return;
        if (!LobbyModule.getLobbyConfig().isCustomSlotAtJoinEnabled()) return;
        event.getPlayer().getInventory().setHeldItemSlot(LobbyModule.getLobbyConfig().getCustomSlotArtJoin());
    }
}

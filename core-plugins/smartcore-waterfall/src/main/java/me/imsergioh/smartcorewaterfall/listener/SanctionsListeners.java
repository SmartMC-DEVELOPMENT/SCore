package me.imsergioh.smartcorewaterfall.listener;

import me.imsergioh.smartcorewaterfall.instance.sanction.PlayerSanction;
import me.imsergioh.smartcorewaterfall.instance.sanction.SanctionType;
import me.imsergioh.smartcorewaterfall.manager.SanctionsManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class SanctionsListeners implements Listener {

    @EventHandler(priority = 64)
    public void send(ServerConnectedEvent event) {
        SanctionsManager.loadSanctions(event.getPlayer());
    }

    @EventHandler(priority = 64)
    public void chat(ChatEvent event) {
        if (event.getMessage().startsWith("/")) return;
        if (!(event.getSender() instanceof ProxiedPlayer player)) return;
        for (PlayerSanction sanction : SanctionsManager.get(player.getUniqueId())) {
            if (!sanction.getType().equals(SanctionType.MUTE)) continue;
            if (sanction.isActive()) {
                sanction.sendPlayerInfo(false);
                event.setCancelled(true);
                break;
            }
        }
    }
}

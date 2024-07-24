package us.smartmc.smartcore.proxy.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import us.smartmc.smartcore.proxy.instance.sanction.PlayerSanction;
import us.smartmc.smartcore.proxy.instance.sanction.SanctionType;
import us.smartmc.smartcore.proxy.manager.SanctionsManager;

public class SanctionsListeners implements Listener {

    @EventHandler(priority = 10)
    public void send(ServerConnectEvent event) {
        SanctionsManager.loadSanctions(event.getPlayer());
    }

    @EventHandler(priority = 10)
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

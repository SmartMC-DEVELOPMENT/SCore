package us.smartmc.smartcore.smartcorevelocity.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;
import us.smartmc.smartcore.smartcorevelocity.instance.sanction.PlayerSanction;
import us.smartmc.smartcore.smartcorevelocity.instance.sanction.SanctionType;
import us.smartmc.smartcore.smartcorevelocity.manager.SanctionsManager;

public class SanctionsListeners {

    @Subscribe(order = PostOrder.LATE)
    public void send(ServerConnectedEvent event) {
        SanctionsManager.loadSanctions(event.getPlayer());
    }

    @Subscribe(order = PostOrder.FIRST)
    public void chat(PlayerChatEvent event) {
        if (event.getMessage().startsWith("/")) return;
        Player player = event.getPlayer();
        for (PlayerSanction sanction : SanctionsManager.get(player.getUniqueId())) {
            if (!sanction.getType().equals(SanctionType.MUTE)) continue;
            if (sanction.isActive()) {
                sanction.sendPlayerInfo(false);
                event.setResult(PlayerChatEvent.ChatResult.denied());
                break;
            }
        }
    }
}

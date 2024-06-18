package us.smartmc.bmotd.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import net.kyori.adventure.text.Component;
import us.smartmc.bmotd.BMotdVelocity;
import us.smartmc.bmotd.manager.MOTDManager;
import us.smartmc.bmotd.util.ChatUtil;

public class JoinEvent {

    private final BMotdVelocity plugin = BMotdVelocity.getPlugin();

    @Subscribe
    public void onHandshake(PreLoginEvent event) {
        MOTDManager motdManager = plugin.getMotdManager();
        String domain = null;
        try {
            domain = event.getConnection().getVirtualHost().get().getHostName().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            event.setResult(PreLoginEvent.PreLoginComponentResult.denied(Component.empty()));
            return;
        }

        if (!motdManager.isAllowedDomain(domain)) {
            event.setResult(PreLoginEvent.PreLoginComponentResult.denied(Component.empty()));
        }
    }

    @Subscribe(order = PostOrder.EARLY)
    public void onEvent(ServerConnectedEvent event){
        MOTDManager motdManager = plugin.getMotdManager();

        if(!motdManager.hasWhitelist(event.getPlayer().getUsername())){
            event.getPlayer().disconnect(ChatUtil.parseToComponent(motdManager.getConfig().getString("messages.not-whitelist")));
        }
    }
}

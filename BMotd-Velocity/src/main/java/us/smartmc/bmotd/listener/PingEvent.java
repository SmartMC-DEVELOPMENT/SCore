package us.smartmc.bmotd.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.InboundConnection;
import com.velocitypowered.api.proxy.server.ServerPing;
import com.velocitypowered.api.util.Favicon;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import us.smartmc.bmotd.BMotdVelocity;
import us.smartmc.bmotd.manager.MOTDManager;

public class PingEvent {

    private final BMotdVelocity plugin = BMotdVelocity.getPlugin();

    @Subscribe
    public void onEvent(ProxyPingEvent event) {
        MOTDManager motdManager = plugin.getMotdManager();

        InboundConnection connection = event.getConnection();
        String domain = null;
        try {
            domain = connection.getVirtualHost().get().getHostName().toLowerCase();
        } catch (Exception e) {
            cancel(event);
            return;
        }

        if (!motdManager.isAllowedDomain(domain)) {
            cancel(event);
            return;
        }

        ServerPing.Builder builder = event.getPing().asBuilder();
        event.setPing(builder.build());

        if (motdManager.getConfig().getBoolean("config.whitelist-enabled")) {
            String insertion = motdManager.getWhitelistComponent(domain).insertion();
            if (insertion == null) insertion = LegacyComponentSerializer.legacyAmpersand().serialize(motdManager.getWhitelistComponent(domain))
                    .replace("&", "§");
            builder.version(new ServerPing.Version(4, insertion));
        }

        if (plugin.getMotdUtil().getMOTDFromDomain(domain.replace(".", "-")) != null) {
            builder.description(plugin.getMotdUtil().getMOTDFromDomain(domain.replace(".", "-")));
            int max = plugin.getMotdUtil().getMaxSlots(domain.replace(".", "-"));
            if (max != -1) builder.maximumPlayers(max);
        } else {
            builder.description(motdManager.getDescriptionComponent());
        }

        Favicon favicon = motdManager.getFavicon(domain);
        if (favicon != null) {
            builder.favicon(motdManager.getFavicon(domain));
        }

        builder.onlinePlayers(motdManager.getOnlineCount());
        event.setPing(builder.build());

        System.out.println("[BMOTD] " + event.getConnection().getRemoteAddress() + " pinged with domain -> " + domain);
    }

    private void cancel(ProxyPingEvent event) {
        event.setPing(event.getPing().asBuilder().onlinePlayers(Integer.MIN_VALUE).description(Component.text(
                "§b§lSmart§3§lProtect §8>§4 Invalid Hostname!\n§7Visit us: §fhttps://smartmc.us"
        )).version(new ServerPing.Version(-1, "NOT COMPATIBLE")).build());
    }

}

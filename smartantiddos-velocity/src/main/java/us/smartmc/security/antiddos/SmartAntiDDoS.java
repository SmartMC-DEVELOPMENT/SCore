package us.smartmc.security.antiddos;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import us.smartmc.security.antiddos.commands.SmartAntiDDoSCommand;
import us.smartmc.security.antiddos.event.PlayerEvents;
import us.smartmc.security.antiddos.handler.ConnectionHandler;
import org.slf4j.Logger;

@Plugin(
        id = "smartantiddos-velocity",
        name = "SmartAntiDDoS-Velocity",
        version = "1.0-SNAPSHOT"
)
public class SmartAntiDDoS {

    @Inject
    private Logger logger;

    @Getter
    private static SmartAntiDDoS plugin;

    @Getter
    private static ProxyServer proxy;

    @Inject
    public SmartAntiDDoS(ProxyServer proxyServer) {
        proxy = proxyServer;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        plugin = this;
        getProxy().getEventManager().register(plugin, new PlayerEvents());
        ConnectionHandler.setup();
        getProxy().getCommandManager().register("smartantiddos", new SmartAntiDDoSCommand());
    }
}

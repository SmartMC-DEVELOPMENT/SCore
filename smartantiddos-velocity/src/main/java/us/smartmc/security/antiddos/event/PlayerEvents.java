package us.smartmc.security.antiddos.event;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import us.smartmc.security.antiddos.handler.ConnectionHandler;
import net.kyori.adventure.text.Component;

import java.net.InetSocketAddress;
import java.util.Optional;

public class PlayerEvents {

    @Subscribe(order = PostOrder.FIRST)
    public void onConnect(ServerConnectedEvent event) {
        ConnectionHandler.registerPlayerConnection(event.getPlayer());
        ConnectionHandler.get(event.getPlayer()).registerEnteredServer(event.getServer().getServerInfo().getName());
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onDisconnect(DisconnectEvent event) {
        ConnectionHandler.unregisterPlayerConnection(event.getPlayer());
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onTryConnect(ServerPreConnectEvent event) {
        ConnectionHandler.detectTrafficPacket();
        if (ConnectionHandler.isAllowed(event.getPlayer())) {
            return;
        }
        if (ConnectionHandler.isUnderAttack()) {
            Optional<InetSocketAddress> optional = event.getPlayer().getVirtualHost();
            optional.ifPresent(inetSocketAddress -> ConnectionHandler.registerPing(inetSocketAddress.getAddress() + ""));
            event.getPlayer().disconnect(Component.empty());
        }
    }

    @Subscribe(order = PostOrder.FIRST)
    public void onPing(ProxyPingEvent event) {
        ConnectionHandler.detectTrafficPacket();
        if (ConnectionHandler.isAllowed(event.getConnection())) {
            return;
        }
        if (ConnectionHandler.isUnderAttack()) {
            event.setPing(event.getPing().asBuilder().description(Component.text("Estamos teniendo problemas para encontrar un Proxy.\nPor favor sé paciente.")).build());
        }
    }
}

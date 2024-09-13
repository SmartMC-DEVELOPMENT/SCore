package us.smartmc.smartcore.proxy.listener;

import io.netty.channel.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import us.smartmc.smartcore.proxy.manager.PacketInterceptorHandler;
import us.smartmc.smartcore.proxy.util.NettyUtils;

public class TestNettyListener implements Listener {

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Channel channel = NettyUtils.getChannel(player);

        if (channel != null) {
            ChannelPipeline pipeline = channel.pipeline();

            // Añadir el handler justo antes de "packet-decoder" para interceptar paquetes entrantes
            pipeline.addBefore("packet-decoder", "packet_interceptor", new PacketInterceptorHandler());
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Channel channel = NettyUtils.getChannel(player);

        if (channel != null) {
            ChannelPipeline pipeline = channel.pipeline();

            pipeline.remove("packet_interceptor");

        }
    }
}

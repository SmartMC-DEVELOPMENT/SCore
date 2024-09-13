package us.smartmc.smartcore.proxy.util;

import io.netty.channel.Channel;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.netty.ChannelWrapper;

public class NettyUtils {

    public static Channel getChannel(ProxiedPlayer player) {
        ChannelWrapper wrapper = ((UserConnection) player).getCh();
        return wrapper.getHandle();
    }

}

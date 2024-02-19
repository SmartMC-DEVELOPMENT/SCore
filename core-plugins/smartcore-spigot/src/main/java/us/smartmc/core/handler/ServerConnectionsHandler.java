package us.smartmc.core.handler;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import us.smartmc.core.util.PluginUtils;

public class ServerConnectionsHandler implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!channel.equals("connectServer")) return;
        String name = new String(bytes);
        PluginUtils.redirectTo(player, name);
    }
}

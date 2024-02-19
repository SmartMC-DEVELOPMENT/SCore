package us.smartmc.core.handler;

import me.imsergioh.pluginsapi.instance.handler.RedisPubSubListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import us.smartmc.core.util.PluginUtils;

import java.nio.charset.StandardCharsets;

public class ServerConnectionsHandler extends RedisPubSubListener {

    public ServerConnectionsHandler() {
        super("connectServer");
    }

    @Override
    public void onMessage(String message) {
        String playerName = message.split(" ")[0];
        String serverName = message.split(" ")[1];
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) return;
        PluginUtils.redirectTo(player, serverName);
    }
}

package us.smartmc.survivaladdon.fixescore.handler;

import me.imsergioh.pluginsapi.instance.handler.RedisPubSubListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.survivaladdon.fixescore.util.PluginUtils;

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

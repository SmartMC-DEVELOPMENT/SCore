package us.smartmc.core.instance.player;

import us.smartmc.core.util.PluginUtils;
import org.bukkit.entity.Player;

public class PlayerServerConnect {

    public PlayerServerConnect(Player player, String server) {
        PluginUtils.sendTo(player, server);
    }

}

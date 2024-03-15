package us.smartmc.core.instance.player;

import me.imsergioh.pluginsapi.util.PluginUtils;
import org.bukkit.entity.Player;

public class PlayerServerConnect {

    public PlayerServerConnect(Player player, String server) {
        PluginUtils.redirectTo(player, server);
    }

}

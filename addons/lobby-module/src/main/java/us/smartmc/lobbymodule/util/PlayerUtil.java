package us.smartmc.lobbymodule.util;

import us.smartmc.core.instance.player.SmartCorePlayer;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public static boolean isVip(Player player) {
        boolean hasPermission = player.hasPermission("smartmc.vip");
        if (!hasPermission) {
            send(player, "lobby", "not_vip");
        }
        return hasPermission;
    }

    public static void send(Player player, String holder, String path) {
        SmartCorePlayer.get(player).sendLanguageMessage(holder, path);
    }

}

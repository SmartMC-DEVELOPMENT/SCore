package us.smartmc.event.eventscore.util;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.smartmc.event.eventscore.handler.MainEventHandler;
import us.smartmc.event.eventscore.instance.BroadcastAccessType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BroadcastUtil {

    public static void broadcastMessage(BroadcastAccessType broadcastAccessType, String message) {
        for (Player player : getPlayers(broadcastAccessType)) {
            player.sendMessage(ChatUtil.parse(player, message));
        }
    }

    public static Collection<Player> getPlayers(BroadcastAccessType type) {
        Set<Player> players = new HashSet<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (type.equals(BroadcastAccessType.ADMIN)) {
                if (player.hasPermission("*") || MainEventHandler.isHoster(player)) {
                    players.add(player);
                }
            } else {
                players.add(player);
            }
        }
        return players;
    }

}

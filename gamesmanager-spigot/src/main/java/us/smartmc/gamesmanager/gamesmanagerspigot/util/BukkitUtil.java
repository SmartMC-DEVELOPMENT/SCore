package us.smartmc.gamesmanager.gamesmanagerspigot.util;

import org.bukkit.Bukkit;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.GameEvent;

public class BukkitUtil {

    public static void callEvent(GameEvent event) {
        Bukkit.getPluginManager().callEvent(event);
    }
}

package us.smartmc.gamescore.util;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public class BukkitUtil {

    public static void callEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

}

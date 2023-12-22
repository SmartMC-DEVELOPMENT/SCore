package us.smartmc.core.instance;

import us.smartmc.core.SmartCore;
import us.smartmc.core.pluginsapi.instance.ConsoleLogger;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

public class SpigotLogger implements ConsoleLogger {

    public static void send(String prefix, ChatColor color, String message) {
        String pluginName = SmartCore.getPlugin().getName();
        String consoleMessage = ChatColor.AQUA + "[" + pluginName + " - " + prefix + "] " + color + message;
        Bukkit.getConsoleSender().sendMessage(consoleMessage);
    }

    @Override
    public void log(String message) {
        send("LOG", ChatColor.GREEN, message);
    }

    @Override
    public void info(String message) {
        send("INFO", ChatColor.BLUE, message);
    }

    @Override
    public void warning(String message) {
        send("WARNING", ChatColor.YELLOW, message);
    }

    @Override
    public void error(String message) {
        send("ERROR", ChatColor.DARK_RED, message);
    }

}

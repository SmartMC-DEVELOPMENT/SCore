package me.imsergioh.smartcorewaterfall.instance;

import me.imsergioh.pluginsapi.instance.ConsoleLogger;
import me.imsergioh.smartcorewaterfall.SmartCoreWaterfall;
import net.md_5.bungee.api.ChatColor;

public class BungeeLogger implements ConsoleLogger {

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

    public static void send(String prefix, ChatColor color, String message) {
        String pluginName = SmartCoreWaterfall.getPlugin().getDescription().getName();
        String consoleMessage = ChatColor.AQUA + "[" + pluginName + " - " + prefix + "] " + color + message;
        SmartCoreWaterfall.getPlugin().getProxy().getConsole().sendMessage(consoleMessage);
    }

}

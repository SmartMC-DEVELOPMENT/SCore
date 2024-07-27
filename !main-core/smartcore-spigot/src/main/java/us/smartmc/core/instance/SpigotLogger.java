package us.smartmc.core.instance;

import me.imsergioh.pluginsapi.instance.ConsoleLogger;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import us.smartmc.core.SmartCore;

public class SpigotLogger implements ConsoleLogger {

    public static void send(String prefix, String color, String message) {
        String pluginName = SmartCore.getPlugin().getName();
        String consoleMessage =  "&b[" + pluginName + " - " + prefix + "] " + color + message;
        Bukkit.getConsoleSender().sendMessage(ChatUtil.parse(consoleMessage));
    }

    @Override
    public void log(String message) {
        send("LOG", "&a", message);
    }

    @Override
    public void info(String message) {
        send("INFO", "&b", message);
    }

    @Override
    public void warning(String message) {
        send("WARNING", "&e", message);
    }

    @Override
    public void error(String message) {
        send("ERROR", "&4", message);
    }

}

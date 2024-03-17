package us.smartmc.core.instance;

import me.imsergioh.pluginsapi.instance.ConsoleLogger;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import us.smartmc.core.SmartCore;

public class SpigotLogger implements ConsoleLogger {

    public static void send(String prefix, String color, String message) {
        String pluginName = SmartCore.getPlugin().getName();
        String consoleMessage =  "<aqua>[" + pluginName + " - " + prefix + "] " + color + message;
        Bukkit.getConsoleSender().sendMessage(Component.text(consoleMessage));
    }

    @Override
    public void log(String message) {
        send("LOG", "<green>", message);
    }

    @Override
    public void info(String message) {
        send("INFO", "<blue>", message);
    }

    @Override
    public void warning(String message) {
        send("WARNING", "<yellow>", message);
    }

    @Override
    public void error(String message) {
        send("ERROR", "<dark_red>", message);
    }

}

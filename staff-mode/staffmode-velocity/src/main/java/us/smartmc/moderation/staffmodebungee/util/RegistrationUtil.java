package us.smartmc.moderation.staffmodebungee.util;

import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class RegistrationUtil {

    public static final String STAFF_PERMISSION = "smartmc.staff";

    public static void registerCommands(Plugin plugin, Command... commands) {
        PluginManager manager = plugin.getProxy().getPluginManager();
        for (Command command : commands) {
            manager.registerCommand(plugin, command);
        }
    }

    public static void registerListeners(Plugin plugin, Listener... listeners) {
        PluginManager manager = plugin.getProxy().getPluginManager();
        for (Listener listener : listeners) {
            manager.registerListener(plugin, listener);
        }
    }

}

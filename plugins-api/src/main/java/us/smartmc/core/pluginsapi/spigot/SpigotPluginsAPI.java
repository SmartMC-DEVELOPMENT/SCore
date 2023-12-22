package us.smartmc.core.pluginsapi.spigot;

import us.smartmc.core.pluginsapi.handler.ItemActionsManager;
import us.smartmc.core.pluginsapi.spigot.command.LanguageCommand;
import us.smartmc.core.pluginsapi.spigot.command.RestartCommand;
import us.smartmc.core.pluginsapi.spigot.item.ItemBuilder;
import us.smartmc.core.pluginsapi.spigot.listener.CorePlayerListeners;
import us.smartmc.core.pluginsapi.spigot.message.LanguageMessages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;

public class SpigotPluginsAPI {

    private static JavaPlugin plugin;

    public static void setup(JavaPlugin pl) {
        plugin = pl;
        LanguageMessages.setup();
        ItemBuilder.setup(plugin);
        ItemActionsManager.registerDefaults();

        registerCommands(new RestartCommand("crestart"));
        Bukkit.getPluginManager().registerEvents(new CorePlayerListeners(), pl);

        PluginCommand languageCommand = plugin.getCommand("language");
        if (languageCommand != null) {
            languageCommand.setExecutor(new LanguageCommand());
        } else {
            System.out.println("Command 'language' not found! (Not registered)");
        }
    }

    public static void registerCommands(BukkitCommand... commands) {
        for (BukkitCommand cmd : commands) {

            try {
                Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + getVersion() + ".CraftServer");
                Object craftServer = craftServerClass.cast(Bukkit.getServer());

                Method getCommandMapMethod = craftServerClass.getDeclaredMethod("getCommandMap");
                getCommandMapMethod.setAccessible(true);
                Object commandMap = getCommandMapMethod.invoke(craftServer);
                getCommandMapMethod.setAccessible(false);

                Method registerMethod = commandMap.getClass().getDeclaredMethod("register", String.class, Command.class);
                registerMethod.setAccessible(true);
                registerMethod.invoke(commandMap, cmd.getName(), cmd);
                registerMethod.setAccessible(false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    private static String getVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

}

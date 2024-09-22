package us.smartmc.gamescore.api;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public interface IGamesCoreAPI {

    void initialize(JavaPlugin plugin);
    JavaPlugin getPlugin();

    default Logger getLogger() {
        return getPlugin().getLogger();
    }

    default void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, getPlugin());
        }
    }
}

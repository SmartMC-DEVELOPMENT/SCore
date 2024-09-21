package us.smartmc.gamescore.api;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public interface IGamesCoreAPI {

    void initialize(JavaPlugin plugin);
    JavaPlugin getPlugin();

    default void registerListeners(String packagePath) {
        Package packageInstance = ClassLoader.getPlatformClassLoader().getDefinedPackage(packagePath);
        Reflections
    }

    default void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, getPlugin());
        }
    }
}

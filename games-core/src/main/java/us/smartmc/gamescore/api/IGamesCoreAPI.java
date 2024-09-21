package us.smartmc.gamescore.api;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.PlayersManager;

import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.logging.Logger;

public interface IGamesCoreAPI {

    void initialize(JavaPlugin plugin);
    JavaPlugin getPlugin();

    default void registerListeners(String packagePath) throws Exception {
        Reflections reflections = new Reflections(packagePath);
        Set<Class<?>> classList = reflections.getSubTypesOf(Object.class);

        for (Class<?> clazz : classList) {
            if (clazz.isAssignableFrom(Listener.class)) {
                Constructor<?> firstConstructor = clazz.getDeclaredConstructors()[0];
                firstConstructor.setAccessible(true);
                Listener listenerObject = (Listener) firstConstructor.newInstance();
                registerListeners(listenerObject);
            }
        }

    }

    default Logger getLogger() {
        return getPlugin().getLogger();
    }

    default void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, getPlugin());
        }
    }
}

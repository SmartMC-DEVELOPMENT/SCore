package us.smartmc.snowgames.manager;

import org.bukkit.Bukkit;
import org.bukkit.World;
import us.smartmc.snowgames.FFAPlugin;

import java.util.function.Consumer;

public class WorldConfigManager {

    private static final FFAPlugin plugin = FFAPlugin.getFFAPlugin();

    public WorldConfigManager() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            forEachWorld(world -> {
                world.setTime(1000);
                if (world.hasStorm()) {
                    world.setWeatherDuration(0);
                    world.setThundering(false);
                    world.setThunderDuration(0);
                    world.setStorm(false);
                }

            });
        }, 0, 10);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            // Day cycle
        forEachWorld(world -> world.setGameRuleValue("doWeatherCycle", "true"));

        // Weather cycle
        forEachWorld(world -> world.setGameRuleValue("doDaylightCycle", "false"));
        }, 2);

    }

    private void forEachWorld(Consumer<World> consumer) {
        Bukkit.getWorlds().forEach(consumer);
    }
}

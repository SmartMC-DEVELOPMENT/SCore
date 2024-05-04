package us.smartmc.core.luckywars.instance.task;

import org.bukkit.Bukkit;
import us.smartmc.core.luckywars.LuckyTowers;

public class BukkitRepeatingTask extends RepeatingTask {

    private static final LuckyTowers plugin = LuckyTowers.getPlugin();

    public BukkitRepeatingTask(boolean async, Runnable runnable) {
       super(getRunnable(async, runnable));
    }

    private static Runnable getRunnable(boolean async, Runnable runnable) {
        if (async) {
            return () -> {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
            };
        }
        return () -> {
            Bukkit.getScheduler().runTask(plugin, runnable);
        };
    }

}

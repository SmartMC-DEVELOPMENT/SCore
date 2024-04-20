package us.smartmc.addon.holograms.manager;

import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import org.bukkit.scheduler.BukkitRunnable;
import us.smartmc.addon.holograms.HologramsAddon;
import us.smartmc.addon.holograms.instance.hologram.HologramHolder;

public class HologramUpdaterManager {

    private static final HologramsAddon addon = HologramsAddon.getPlugin();

    private static BukkitRunnable runnable;

    public static void startRunnable() {
        if (runnable != null) return;
        runnable = new BukkitRunnable() {
            int currentTick;
            @Override
            public void run() {
                if (currentTick % getUpdateRateTicks() == 0) {
                    update();
                }
                currentTick++;
            }
        };
        runnable.runTaskTimerAsynchronously(SpigotPluginsAPI.getPlugin(), getUpdateRateTicks(), 1);
    }

    private static void update() {
        // Update name
        HologramHolder.forEachHolder(hologramHolder -> {
            hologramHolder.forEachHologram(hologram -> {
                addon.getHologramAdapter().updateHologramMetaData();
            });
        });
    }

    private static int getUpdateRateTicks() {
        return addon.getConfig().getUpdateRateTicks();
    }

}

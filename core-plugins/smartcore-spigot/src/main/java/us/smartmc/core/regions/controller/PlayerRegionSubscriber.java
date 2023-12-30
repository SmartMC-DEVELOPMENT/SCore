package us.smartmc.core.regions.controller;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import us.smartmc.core.SmartCore;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.core.regions.CuboidManager;
import us.smartmc.core.regions.RegionConfig;

import java.util.*;

@Getter
public class PlayerRegionSubscriber {

    private static final SmartCore plugin = SmartCore.getPlugin();
    private static final Map<UUID, String> registry = new HashMap<>();
    private static final CuboidManager cuboidManager = plugin.getCuboidManager();

    private final SmartCorePlayer corePlayer;
    private double x, y, z;
    private int taskID;
    private final Set<RegionConfig> regionsAt = new HashSet<>();

    public PlayerRegionSubscriber(SmartCorePlayer corePlayer) {
        this.corePlayer = corePlayer;
        startTask();
    }

    // This will be executed when x, y or z changes
    private void executeAtXYZChange() {
        Location location = corePlayer.get().getLocation();
        // Remove regions not in & call event leave

        new HashSet<>(regionsAt).forEach(region -> {
            if (region.getCuboidRegion().contains(location)) return;
            regionsAt.remove(region);
        });

        // Add regions in & call event enter
        cuboidManager.values().forEach(region -> {
            if (regionsAt.contains(region)) return;
            if (!region.getCuboidRegion().contains(location)) return;
            regionsAt.add(region);
        });
    }

    private void startTask() {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            Location location = corePlayer.get().getLocation();
            if (location.getX() == x && location.getY() == y && location.getZ() == z) return;
            x = location.getX();
            y = location.getY();
            z = location.getZ();
            executeAtXYZChange();
        }, 0, 10);
    }

}

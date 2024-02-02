package us.smartmc.snowgames.manager;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import us.smartmc.snowgames.config.DefaultConfig;
import us.smartmc.snowgames.object.BlockCooldownTask;
import us.smartmc.snowgames.util.WorldUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BlocksResetManager {

    private static final Set<BlockCooldownTask> tasks = new HashSet<>();

    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public static void registerBlockPlace(BlockState block) {
        BlockCooldownTask task = new BlockCooldownTask(block);
        executorService.schedule(task, DefaultConfig.getCooldown("blockRestoration"), TimeUnit.SECONDS);
        tasks.add(task);
    }

    public static void completeAllByWorldName(String worldName) {
        World world = Bukkit.getWorld(worldName);
        for (BlockCooldownTask task : tasks) {
            if (!task.getLocation().getWorld().equals(world)) continue;
            task.reset();
        }
    }

}

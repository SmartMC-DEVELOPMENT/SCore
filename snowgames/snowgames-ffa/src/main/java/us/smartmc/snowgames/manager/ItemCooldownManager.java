package us.smartmc.snowgames.manager;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.smartmc.snowgames.object.ItemCooldownTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ItemCooldownManager {

    private static final HashMap<UUID, ItemCooldownManager> managers = new HashMap<>();

    @Getter
    private final Player player;

    private final HashMap<Integer, ItemCooldownTask> activeTasks = new HashMap<>();

    private ScheduledExecutorService executorService;

    private ItemCooldownManager(Player player) {
        this.player = player;
        executorService = Executors.newScheduledThreadPool(10);
    }

    public void registerAt(int slot, long period) {
        if (activeTasks.containsKey(slot)) return;
        ItemCooldownTask task = new ItemCooldownTask(this, slot, period);

        activeTasks.put(slot, task);
        executorService.scheduleAtFixedRate(task, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void cancelAll(boolean complete) {
        if (complete) {
            for (ItemCooldownTask task : new HashSet<>(activeTasks.values())) {
                task.completeTask();
            }
        }
        executorService.shutdown();
        executorService = Executors.newScheduledThreadPool(10);
    }

    public void remove(int slot) {
        activeTasks.remove(slot);
    }

    public boolean hasActiveAtSlot(int slot) {
        return activeTasks.containsKey(slot);
    }

    public static void clear(Player player) {
        ItemCooldownManager manager = managers.get(player.getUniqueId());
        if (manager == null) return;
        manager.cancelAll(false);
        managers.remove(player.getUniqueId());
    }

    public static ItemCooldownManager from(Player player) {
        return managers.computeIfAbsent(player.getUniqueId(), uuid -> new ItemCooldownManager(player));
    }

}
package us.smartmc.snowgames.object;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.snowgames.manager.ItemCooldownManager;

public class ItemCooldownTask extends PluginRepeatingTask {

    protected final ItemCooldownManager manager;

    protected final Player player;
    protected final int slot, recoverAmount;
    protected final ItemStack recoverItem;

    public ItemCooldownTask(ItemCooldownManager manager, int slot, long seconds) {
        super(1000 * seconds);
        this.manager = manager;
        this.player = manager.getPlayer();
        this.slot = slot;
        ItemStack item = player.getInventory().getItem(slot);
        this.recoverAmount = item.getAmount();
        recoverItem = item.clone();

        onDelay(() -> {
            int remainingSeconds = (int) getRemainingTimeInSeconds();
            ItemStack delayItem = recoverItem.clone();
            delayItem.setAmount(-1 * remainingSeconds);
            player.getInventory().setItem(slot, delayItem);
        });

        onComplete(() -> {
            player.getInventory().setItem(slot, recoverItem);
            manager.remove(slot);
        });
    }

    public void completeTask() {
        active = false;
        if (completeRunnable != null)
            completeRunnable.run();
        cancel();
    }

}

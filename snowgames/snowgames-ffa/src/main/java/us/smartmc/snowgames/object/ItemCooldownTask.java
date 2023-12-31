package us.smartmc.snowgames.object;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.game.FFAGame;
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
            FFAGame game = FFAPlugin.getGame();
            if (!game.isInGame(player)) {
                completeTask();
                return;
            }
            int remainingSeconds = (int) getRemainingTimeInSeconds();
            if (recoverItem.getType().equals(Material.GOLD_PLATE)) {
                ItemStack delayItem = new ItemStack(Material.STONE_PLATE);
                delayItem.setAmount(-1 * remainingSeconds);
                player.getInventory().setItem(slot, delayItem);
                return;
            }
            if (recoverItem.getType().equals(Material.FEATHER)) {
                ItemStack delayItem = new ItemStack(Material.SUGAR);
                delayItem.setAmount(-1 * remainingSeconds);
                player.getInventory().setItem(slot, delayItem);
            }
        });

        onComplete(() -> {
            FFAGame game = FFAPlugin.getGame();
            if (!game.isInGame(player)) {
                completeTask();
                manager.remove(slot);
                return;
            }
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

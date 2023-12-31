package us.smartmc.snowgames.object;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.config.LanguageConfig;
import us.smartmc.snowgames.game.FFAGame;
import us.smartmc.snowgames.manager.ItemCooldownManager;

import static us.smartmc.snowgames.inventory.FFAMenu.parseItem;

public class ItemCooldownTask extends PluginRepeatingTask {

    protected final ItemCooldownManager manager;

    private static final LanguageConfig config = FFAPlugin.getPlugin().getLanguageConfig();

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
                Language language = CorePlayer.get(player).getLanguage();
                ItemStack itemStack = parseItem(player, config.getItemConfig(language, "propeller_reloading").get(), "&e");
                ItemMeta meta = itemStack.getItemMeta();
                String name = meta.getDisplayName();
                ItemStack delayItem = ItemBuilder.of(Material.STONE_PLATE).name(name).get();
                delayItem.setAmount(remainingSeconds);
                player.getInventory().setItem(slot, delayItem);
                return;
            }
            if (recoverItem.getType().equals(Material.FEATHER)) {
                Language language = CorePlayer.get(player).getLanguage();
                ItemStack itemStack = parseItem(player, config.getItemConfig(language, "speed_reloading").get(), "&a");
                ItemMeta meta = itemStack.getItemMeta();
                String name = meta.getDisplayName();
                ItemStack delayItem = ItemBuilder.of(Material.SUGAR).name(name).get();
                delayItem.setAmount(remainingSeconds);
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

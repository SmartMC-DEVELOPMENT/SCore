package us.smartmc.snowgames.object;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.config.LanguageConfig;
import us.smartmc.snowgames.game.FFAGame;
import us.smartmc.snowgames.manager.ItemCooldownManager;

import static us.smartmc.snowgames.menu.FFAMenu.parseItem;

public class ItemCooldownTask extends PluginRepeatingTask {

    protected final ItemCooldownManager manager;

    private static final LanguageConfig config = FFAPlugin.getFFAPlugin().getLanguageConfig();

    protected final Player player;
    protected final GamePlayer gamePlayer;
    protected final int slot, recoverAmount;
    protected final ItemStack recoverItem;

    public ItemCooldownTask(ItemCooldownManager manager, int slot, long seconds) {
        super(1000 * seconds);
        this.manager = manager;
        this.player = manager.getPlayer();
        gamePlayer = FFAPlugin.getFFAPlugin().getGamePlayerManager().get(player.getUniqueId());
        this.slot = slot;
        ItemStack item = player.getInventory().getItem(slot);
        this.recoverAmount = item.getAmount();
        recoverItem = item.clone();

        onDelay(() -> {
            FFAGame game = FFAPlugin.getGame();
            if (!game.isInGame(gamePlayer)) {
                completeTask();
                return;
            }
            int remainingSeconds = (int) getRemainingTimeInSeconds();
            if (recoverItem.getType().equals(Material.GOLD_PLATE)) {
                Language language = PlayerLanguages.get(player.getUniqueId());
                ItemStack delayItem = parseItem(player, config.getItemConfig(language, "propeller_reloading").get(), "&e");
                delayItem.setAmount(remainingSeconds);
                player.getInventory().setItem(slot, delayItem);
            }
            if (recoverItem.getType().equals(Material.FEATHER)) {
                Language language = PlayerLanguages.get(player.getUniqueId());
                ItemStack delayItem = parseItem(player, config.getItemConfig(language, "speed_reloading").get(), "&a");
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

package us.smartmc.snowgames.util;

import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.config.DefaultConfig;
import us.smartmc.snowgames.config.LanguageConfig;
import us.smartmc.snowgames.inventory.GameHotbar;
import us.smartmc.snowgames.manager.ItemCooldownManager;

public class GameItemUtils {


    public static void openMenu(Player player, Class<? extends CoreMenu> menuClass) {
        try {
            CoreMenu menu = menuClass.getDeclaredConstructor(Player.class).newInstance(player);
            menu.open(player);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calcula la suma de dos números.
     *
     * @param player es killer
     */
    public static void handlePlayerKill(Player player) {
        ItemStack regenerationItem = player.getInventory().getItem(8);

        if (regenerationItem != null) {
            int amount = regenerationItem.getAmount();
            if (amount <= 4) {
                amount++;
            }
            regenerationItem.setAmount(amount);
        } else {
            Language language = CorePlayer.get(player).getLanguage();
            LanguageConfig config = FFAPlugin.getPlugin().getLanguageConfig();

            player.getInventory().setItem(8, GameHotbar.parseItem(player,
                    config.getItemConfig(language, "item.regeneration").get(), "&a"));
        }
    }

    public static void handleVelocityAction(Player clicker) {
        clicker.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                20 * 5, 1));
        ItemCooldownManager.from(clicker)
                .registerAt(clicker.getInventory().getHeldItemSlot(),
                        DefaultConfig.getCooldown("speed"));
    }

    public static void handleWeaponAction(Player shooter) {
        Snowball snowball = shooter.launchProjectile(Snowball.class);
        snowball.setVelocity(shooter.getEyeLocation().getDirection().multiply(3.5D));
        snowball.setShooter(shooter);
    }
}

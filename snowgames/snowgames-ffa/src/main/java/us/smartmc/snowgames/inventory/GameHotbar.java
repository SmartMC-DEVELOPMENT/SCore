package us.smartmc.snowgames.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.smartmc.core.pluginsapi.language.Language;
import us.smartmc.core.pluginsapi.spigot.item.ItemBuilder;
import us.smartmc.core.pluginsapi.spigot.menu.CoreMenu;
import us.smartmc.core.pluginsapi.spigot.player.CorePlayer;
import us.smartmc.core.pluginsapi.util.ChatUtil;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.config.LanguageConfig;
import us.smartmc.snowgames.util.GameItemUtils;

public class GameHotbar extends FFAMenu {

    private static final FFAPlugin plugin = FFAPlugin.getPlugin();
    private static final LanguageConfig config = plugin.getLanguageConfig();


    protected GameHotbar(Player player) {
        super(player, 9, "game_hotbar");
    }

    @Override
    public void load() {
        Language language = CorePlayer.get(player).getLanguage();
        set(0, parseItem(player, config.getItemConfig(language, "weapon").get(), "&b"), "game weapon");

        set(1, parseItem(player, config.getItemConfig(language, "blocks").get(), "&b"));

        set(4, parseItem(player, config.getItemConfig(language, "propeller").get(), "&e"));

        set(7, parseItem(player, config.getItemConfig(language, "speed")
                .get(), "&a"), "game speed");

        set(8, parseItem(player, config.getItemConfig(language, "regeneration").get(), "&a"), "game regeneration");
    }

    @Override
    public void set(Player player) {
        player.getInventory().setContents(getInventory().getContents());
        CorePlayer.get(player).setCurrentMenuSet(this);
    }

    public static void give(Player player) {
        GameHotbar hotBar = new GameHotbar(player);
        player.getInventory().clear();
        hotBar.set(player);
        player.setHealthScale(20);
        player.setFoodLevel(20);
    }

}

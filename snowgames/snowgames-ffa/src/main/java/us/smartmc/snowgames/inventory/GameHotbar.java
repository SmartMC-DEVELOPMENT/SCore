package us.smartmc.snowgames.inventory;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.entity.Player;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.config.LanguageConfig;
import us.smartmc.snowgames.menu.FFAMenu;

public class GameHotbar extends FFAMenu {

    private static final FFAPlugin plugin = FFAPlugin.getFFAPlugin();
    private static final LanguageConfig config = plugin.getLanguageConfig();

    protected GameHotbar(Player player) {
        super(player, 9, "game_hotbar");
    }

    @Override
    public void load() {
        Language language = PlayerLanguages.get(initPlayer.getUniqueId());
        if (language == null) return;
        set(0, parseItem(initPlayer, config.getItemConfig(language, "weapon").get(), "&b"), "game weapon");

        set(1, parseItem(initPlayer, config.getItemConfig(language, "blocks").get(), "&b"));

        set(4, parseItem(initPlayer, config.getItemConfig(language, "propeller").get(), "&e"));

        set(7, parseItem(initPlayer, config.getItemConfig(language, "speed")
                .get(), "&a"), "game speed");

        set(8, parseItem(initPlayer, config.getItemConfig(language, "regeneration").get(), "&a"), "game regeneration");
    }

    @Override
    public void set(Player player) {
        load();
        player.getInventory().setContents(getInventory().getContents());
        CorePlayer.get(player).setCurrentMenuSet(this);
    }

    public static void give(Player player) {
        GameHotbar hotBar = new GameHotbar(player);
        player.getInventory().clear();
        hotBar.set(player);
        player.setHealthScale(20);
        player.setFoodLevel(20);
        player.updateInventory();
    }

}

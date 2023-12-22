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

public class LobbyHotbar extends FFAMenu {

    private static final FFAPlugin plugin = FFAPlugin.getPlugin();
    private static final LanguageConfig config = plugin.getLanguageConfig();


    private LobbyHotbar(Player player) {
        super(player, 9, "lobby_hotbar");
    }

    @Override
    public void load() {
        Language language = CorePlayer.get(player).getLanguage();

        set(0, parseItem(player, config.getItemConfig(language, "lobby.tops").get(player)),
                "hotbar tops");

        set(4, parseItem(player, config.getItemConfig(language, "lobby.settings").get(player)),
                "hotbar settings");
    }

    public static ItemStack parseItem(Player player, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return ItemBuilder.of(item.getType()).get(player);
        String name = meta.getDisplayName();
        if (name == null) return ItemBuilder.of(item.getType()).get(player);
        return ItemBuilder.of(item.getType()).amount(item.getAmount()).name(ChatUtil.parse(player, name)).get(player);
    }

    public static void give(Player player) {
        LobbyHotbar hotBar = new LobbyHotbar(player);
        player.getInventory().clear();
        hotBar.set(player);
        player.setHealthScale(20);
        player.setFoodLevel(20);
        CorePlayer.get(player).setCurrentMenuSet(hotBar);
    }

}

package us.smartmc.snowgames.inventory;

import me.imsergioh.pluginsapi.item.ItemBuilder;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.config.LanguageConfig;

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

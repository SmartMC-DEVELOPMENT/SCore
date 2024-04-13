package us.smartmc.snowgames.inventory;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.config.LanguageConfig;
import us.smartmc.snowgames.menu.FFAMenu;

import java.util.UUID;

public class LobbyHotbar extends FFAMenu {

    private static final FFAPlugin plugin = FFAPlugin.getFFAPlugin();
    private static final LanguageConfig config = plugin.getLanguageConfig();

    private LobbyHotbar(Player player) {
        super(player, 9, UUID.randomUUID().toString());
    }

    @Override
    public void load() {
        Language language = PlayerLanguages.get(initPlayer.getUniqueId());

        set(0, parseItem(initPlayer, config.getItemConfig(language, "lobby.tops").get(initPlayer)),
                "hotbar tops");

        set(4, parseItem(initPlayer, config.getItemConfig(language, "lobby.settings").get(initPlayer)),
                "hotbar settings");
    }

    public static ItemStack parseItem(Player player, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return ItemBuilder.of(item.getType()).get(player);
        String name = meta.getDisplayName();
        if (name == null) return ItemBuilder.of(item.getType()).get(player);
        return ItemBuilder.of(item.getType()).amount(item.getAmount()).name(name).get(player);
    }
}

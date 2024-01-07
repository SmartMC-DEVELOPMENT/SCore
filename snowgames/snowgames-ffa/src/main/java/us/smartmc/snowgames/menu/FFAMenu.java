package us.smartmc.snowgames.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class FFAMenu extends CoreMenu {

    protected FFAMenu(Player player, int size, String title) {
        super(player, size, title);
    }

    public static ItemStack parseItem(Player player, ItemStack item, String prefix) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return ItemBuilder.of(item.getType()).get(player);
        String name = meta.getDisplayName();
        if (name == null) return ItemBuilder.of(item.getType()).get(player);
        return ItemBuilder.of(item.getType()).amount(item.getAmount()).name(ChatUtil.parse(player, prefix + name)).get(player);
    }

    protected static ItemStack parseItem(Player player, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return ItemBuilder.of(item.getType()).get(player);
        String name = meta.getDisplayName();
        if (name == null) return ItemBuilder.of(item.getType()).get(player);
        return ItemBuilder.of(item.getType()).amount(item.getAmount()).name(ChatUtil.parse(player, name)).get(player);
    }

}

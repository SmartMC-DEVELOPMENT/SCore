package us.smartmc.snowgames.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class FFAMenu extends CoreMenu {

    protected FFAMenu(Player player, int size, String title) {
        super(player, size, title);
    }

    public static ItemStack parseItem(Player player, ItemStack item, String prefix) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return ItemBuilder.of(item.getType()).get(player);
        String name = meta.getDisplayName();
        if (name == null) return ItemBuilder.of(item.getType()).get(player);
        return ItemBuilder.of(item.getType()).amount(item.getAmount()).name(prefix + name).get(player);
    }

    protected static ItemStack parseItem(Player player, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return ItemBuilder.of(item.getType()).get(player);
        String name = meta.getDisplayName();
        if (name == null) return ItemBuilder.of(item.getType()).get(player);
        return ItemBuilder.of(item.getType()).amount(item.getAmount()).name(name).get(player);
    }

    public static void give(Player player, Class<? extends FFAMenu> menuClass) {
        FFAMenu hotBar = null;
        try {
            Constructor<? extends FFAMenu> constructor = menuClass.getDeclaredConstructor(Player.class);
            constructor.setAccessible(true);
            hotBar = constructor.newInstance(player);
            player.getInventory().clear();
            hotBar.set(player);
            player.setHealthScale(20);
            player.setFoodLevel(20);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }

}

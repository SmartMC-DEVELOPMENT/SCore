package us.smartmc.core.pluginsapi.spigot.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.smartmc.core.pluginsapi.handler.MenuItemActionManager;
import us.smartmc.core.pluginsapi.spigot.item.ItemBuilder;
import us.smartmc.core.pluginsapi.spigot.player.CorePlayer;
import us.smartmc.core.pluginsapi.util.ChatUtil;

import java.util.Arrays;
import java.util.HashMap;

public abstract class CoreMenu implements ICoreMenu {

    protected final Player player;
    protected final int size;
    protected final Inventory inventory;

    protected final MenuItemActionManager actionManager = new MenuItemActionManager();

    protected CoreMenu(Player player, int size, String title) {
        this.player = player;
        this.size = size;
        this.inventory = Bukkit.createInventory(null, size, ChatUtil.parse(player, title));
        load();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public ItemStack get(int slot) {
        return inventory.getItem(slot);
    }

    @Override
    public void set(int slot, ItemStack item) {
        inventory.setItem(slot, item);
    }

    @Override
    public void set(int slot, ItemStack item, String... labelCommands) {
        inventory.setItem(slot, item);
        actionManager.registerItemAction(slot, item, Arrays.asList(labelCommands));
    }

    @Override
    public void open(Player player) {
        player.openInventory(inventory);
        CorePlayer.get(player).setCurrentMenuOpen(this);
        player.playSound(player.getLocation(), Sound.CLICK, 0.1F, 2.5F);
    }

    @Override
    public void set(Player player) {
        CorePlayer.get(player).setCurrentMenuSet(this);
        player.getInventory().setContents(inventory.getContents());
        player.updateInventory();
    }

    public ItemStack getPreviousMenuItem(Player player) {
        ItemStack item = ItemBuilder.of(Material.BOOK)
                .name("&c<lang.language.menu_previous>").get(player);
        return item;
    }

    public ItemStack getCloseMenuItem(Player player) {
        return ItemBuilder.of(Material.BARRIER)
                .name("&c<lang.language.menu_close>").get(player);
    }

    public MenuItemActionManager getActionManager() {
        return actionManager;
    }
}

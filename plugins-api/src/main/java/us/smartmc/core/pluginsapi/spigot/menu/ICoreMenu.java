package us.smartmc.core.pluginsapi.spigot.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface ICoreMenu {

    void load();

    Inventory getInventory();

    ItemStack get(int slot);
    void set(int slot, ItemStack item);
    void set(int slot, ItemStack item, String... labelCommands);

    void open(Player player);
    void set(Player player);
}

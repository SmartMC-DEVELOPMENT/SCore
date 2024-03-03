package us.smartmc.event.eventscore.util;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuUtil {

    public static void setBorderMenu(ItemStack item, CoreMenu menu) {
        setRow(0, item, menu);
        setRow(menu.getInventory().getSize() / 9, item, menu);
        setColumn(0, item, menu);
        setColumn(8, item, menu);
    }

    public static void setRow(int rowSlot, ItemStack item, CoreMenu menu) {
        Inventory inventory = menu.getInventory();
        int start = rowSlot * 9;
        setFromTo(start, start + 8, item, inventory);
    }

    public static void setColumn(int columnSlot, ItemStack item, CoreMenu menu) {
        Inventory inventory = menu.getInventory();
        int end = (inventory.getSize() / 9) - columnSlot;
        setFromTo(columnSlot, end, 9, item, inventory);
    }

    private static void setFromTo(int from, int to, ItemStack item, Inventory inventory) {
        setFromTo(from, to, 1, item, inventory);
    }

    private static void setFromTo(int from, int to, int jumpsPerSlot, ItemStack item, Inventory inventory) {
        for (int slot = from; slot < to; slot += jumpsPerSlot) {
            if (slot == inventory.getSize()) break;
            inventory.setItem(slot, item);
        }
    }

}

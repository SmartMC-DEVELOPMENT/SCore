package us.smartmc.lobbymodule.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MenuUtil {

    public static void setCorners(ItemStack item, Inventory inventory) {
        int[] slots = {0, 1, 9, inventory.getSize() - 1, inventory.getSize() - 2, inventory.getSize() - 10};
        for (int slot : slots) {
            inventory.setItem(slot, item);
        }
    }

    public static void fill(ItemStack item, Inventory inventory) {
        for (int slot = 0; slot < inventory.getSize(); slot++) {
            inventory.setItem(slot, item);
        }
    }

    public static void setColumn(int columLine, ItemStack item, Inventory inventory) {
        int rows = inventory.getSize() / 9;
        int times = 0;
        for (int slot = columLine; times < rows; slot += 9) {
            inventory.setItem(slot, item);
            times++;
        }
    }

    public static void setRow(int rowLine, ItemStack item, Inventory inventory) {
        int start = 9 * rowLine;
        int end = start + 9;
        for (int slot = start; slot < end; slot++) {
            inventory.setItem(slot, item);
        }
    }

}

package us.smartmc.core.pluginsapi.handler;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.smartmc.core.pluginsapi.spigot.item.ClickHandler;

import java.util.HashMap;
import java.util.List;

public class MenuItemActionManager {

    private final HashMap<String, List<String>> slotConfigActions = new HashMap<>();

    public void registerItemAction(int slot, ItemStack item, List<String> list) {
        if (list.isEmpty()) return;
        slotConfigActions.put(itemID(slot, item), list);
    }

    public boolean execute(int slot, ItemStack item, ClickHandler handler) {
        List<String> labelActions = slotConfigActions.get(itemID(slot, item));
        if (labelActions == null) return false;
        if (labelActions.isEmpty()) return false;
        return ItemActionsManager.execute(handler, labelActions);
    }

    private String itemID(int slot, ItemStack item) {
        String stackID = stackID(item);
        return stackID + "@" + slot;
    }

    private String stackID(ItemStack item) {
        StringBuilder b = new StringBuilder(item.getType().name() + item.getAmount());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (meta.getDisplayName() != null) b.append(meta.getDisplayName());
            if (meta.getLore() != null) b.append(meta.getLore());
        }
        return b.toString();
    }

}

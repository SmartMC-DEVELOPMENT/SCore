package us.smartmc.core.pluginsapi.spigot.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.smartmc.core.pluginsapi.spigot.item.ClickHandler;
import us.smartmc.core.pluginsapi.spigot.menu.CoreMenu;
import us.smartmc.core.pluginsapi.spigot.player.CorePlayer;

public class ItemActionListeners implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void cancelClickMenuAtOpenMenu(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        // Cancel click event if core menu exists for clicked inventory
        if (get(player, player.getOpenInventory().getTopInventory()) != null) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void cancelClickMenuAtSetMenu(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        // Cancel click event if core menu exists for clicked inventory
        if (get(player, player.getInventory()) != null) event.setCancelled(true);
    }

    @EventHandler
    public void actionInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        // Execute actions with given properties
        CoreMenu coreMenu = CorePlayer.get(player).getCurrentMenuSet();
        if (coreMenu == null) return;
        if (item == null) return;
        action(player.getInventory().getHeldItemSlot(), item, player, event);
    }

    @EventHandler
    public void actionClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (event.getCurrentItem() == null) return;
        Player player = (Player) event.getWhoClicked();

        // Execute actions with given properties
        action(event.getSlot(), event.getCurrentItem(), player, event);
    }

    private void action(int slot, ItemStack item, Player player, Event event) {
        if (item == null) return;
        ClickHandler handler = new ClickHandler(player, item, event);
        if (CorePlayer.get(player).getCurrentMenuOpen() != null) {
            CorePlayer.get(player).getCurrentMenuOpen().getActionManager().execute(slot, item, handler);
            return;
        }

        if (CorePlayer.get(player).getCurrentMenuSet() != null) {
            CorePlayer.get(player).getCurrentMenuSet().getActionManager().execute(slot, item, handler);
        }
    }

    public CoreMenu get(Player player, Inventory inventory) {
        CorePlayer corePlayer = CorePlayer.get(player);
        CoreMenu menu = corePlayer.getCurrentMenuOpen();
        if (inventory == null) return null;
        try {
            if (menu != null && inventory.equals(menu.getInventory())) return menu;
        } catch (Exception ignore) {
        }
        try {
            menu = corePlayer.getCurrentMenuSet();
            if (menu != null && inventory.equals(menu.getInventory())) return menu;
        } catch (Exception ignore) {
        }
        return null;
    }
}

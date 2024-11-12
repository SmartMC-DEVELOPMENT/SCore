package us.smartmc.core.listener;

import org.bukkit.event.player.PlayerInteractEvent;
import us.smartmc.core.SmartCore;
import us.smartmc.core.handler.AdminModeHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class AdminModeListeners implements Listener {

    private static final AdminModeHandler handler = SmartCore.getPlugin().getAdminModeHandler();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!handler.isActive(player)) return;
        event.setCancelled(false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (!handler.isActive(player)) return;
        event.setCancelled(false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!handler.isActive(player)) return;
        event.setCancelled(false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!handler.isActive(player)) return;
        event.setCancelled(false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (!handler.isActive(player)) return;
        event.setCancelled(false);
    }

}

package us.smartmc.lobbymodule.listener;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import us.smartmc.core.pluginsapi.util.SyncUtil;
import us.smartmc.smartaddons.plugin.AddonListener;

import java.util.*;

public class InventoryListeners extends AddonListener implements Listener {

    private static final List<InventoryType> allowed = Arrays.asList(
            InventoryType.CREATIVE, InventoryType.PLAYER, InventoryType.CHEST);

    @EventHandler(priority = EventPriority.MONITOR)
    public void onOpenNotInv(InventoryOpenEvent event) {
        if (!isEnabled()) return;
        if (allowed.contains(event.getInventory().getType())) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDrop(PlayerDropItemEvent event) {
        if (!isEnabled()) return;
        event.setCancelled(true);
    }

}

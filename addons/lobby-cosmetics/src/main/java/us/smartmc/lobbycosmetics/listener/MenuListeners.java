package us.smartmc.lobbycosmetics.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.lobbycosmetics.LobbyCosmetics;
import us.smartmc.lobbycosmetics.handler.CosmeticSessionHandler;
import us.smartmc.lobbycosmetics.instance.player.CosmeticPlayerSession;
import us.smartmc.lobbycosmetics.menu.CosmeticBuyMenu;
import us.smartmc.smartaddons.plugin.AddonListener;

public class MenuListeners extends AddonListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void clearBuyCache(InventoryCloseEvent event) {
        SmartCorePlayer smartCorePlayer = SmartCorePlayer.get(event.getPlayer().getUniqueId());
        if (smartCorePlayer == null) return;
        if (smartCorePlayer.getCurrentMenuOpen() instanceof CosmeticBuyMenu) {
            CosmeticBuyMenu.clearCache(event.getPlayer().getUniqueId());
        }
    }
}

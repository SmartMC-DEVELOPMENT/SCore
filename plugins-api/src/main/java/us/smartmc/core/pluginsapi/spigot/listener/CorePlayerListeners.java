package us.smartmc.core.pluginsapi.spigot.listener;

import org.bukkit.entity.Player;
import us.smartmc.core.pluginsapi.spigot.item.ItemBuilder;
import us.smartmc.core.pluginsapi.spigot.menu.CoreMenu;
import us.smartmc.core.pluginsapi.spigot.player.CorePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CorePlayerListeners implements Listener {

    @EventHandler
    public void registerInventoryCloseInv(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();
        CoreMenu menu = CorePlayer.get(player).getCurrentMenuOpen();
        if (menu == null) return;
        CorePlayer.get(event.getPlayer().getUniqueId()).registerMenuHistory(menu);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        //CorePlayer.register(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        //CorePlayer.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();
        CorePlayer.get(player).setCurrentMenuOpen(null);
    }
}

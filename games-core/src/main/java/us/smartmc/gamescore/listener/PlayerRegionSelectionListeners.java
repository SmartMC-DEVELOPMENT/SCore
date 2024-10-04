package us.smartmc.gamescore.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import us.smartmc.gamescore.adminplayer.PlayerRegionSelectSession;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.manager.player.PlayerRegionSelectionsManager;

public class PlayerRegionSelectionListeners implements Listener {

    public static void register() {
        Bukkit.getPluginManager().registerEvents(new PlayerRegionSelectionListeners(), GamesCoreAPI.getApi().getPlugin());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void handlePosition1(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!PlayerRegionSelectionsManager.canWand(player)) return;
        if (event.getClickedBlock() == null) return;
        if (!event.getAction().name().contains("RIGHT")) return;
        PlayerRegionSelectionsManager manager = getManager();
        PlayerRegionSelectSession selectSession = manager.getOrCreate(player.getUniqueId());
        Block block = event.getClickedBlock();
        selectSession.setPos1(block.getLocation());
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void handlePosition2(BlockBreakEvent event) {
        handlePos2(event.getPlayer(), event.getBlock().getLocation(), event);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void handlePosition2(BlockDamageEvent event) {
        handlePos2(event.getPlayer(), event.getBlock().getLocation(), event);
    }

    private static void handlePos2(Player player, Location blockLocation, Cancellable event) {
        if (!PlayerRegionSelectionsManager.canWand(player)) return;
        PlayerRegionSelectionsManager manager = getManager();
        PlayerRegionSelectSession selectSession = manager.getOrCreate(player.getUniqueId());
        selectSession.setPos2(blockLocation);
        event.setCancelled(true);
    }

    private static PlayerRegionSelectionsManager getManager() {
        return PlayerRegionSelectionsManager.getManager(PlayerRegionSelectionsManager.class);
    }

}

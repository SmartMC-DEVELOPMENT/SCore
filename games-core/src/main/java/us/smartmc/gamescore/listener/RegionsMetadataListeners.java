package us.smartmc.gamescore.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import us.smartmc.gamescore.instance.cuboid.CuboidRegion;
import us.smartmc.gamescore.util.RegionUtils;

public class RegionsMetadataListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelPvP(EntityDamageByEntityEvent event) {
        cancelIfActive(event, event.getEntity().getLocation(), "DENY@PVP");
        cancelIfActive(event, event.getDamager().getLocation(), "DENY@PVP");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelDamage(EntityDamageEvent event) {
        cancelIfActive(event, event.getEntity().getLocation(), "DENY@DAMAGE");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelDrops(PlayerDropItemEvent event) {
        cancelIfActive(event, event.getItemDrop().getLocation(), "DENY@DROPS");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelBlockBreak(BlockBreakEvent event) {
        cancelIfActive(event, event.getBlock().getLocation(), "DENY@BLOCK_BREAK");
        cancelIfActive(event, event.getBlock().getLocation(), "DENY@BLOCKS");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelBlockPlace(BlockPlaceEvent event) {
        cancelIfActive(event, event.getBlock().getLocation(), "DENY@BLOCK_PLACE");
        cancelIfActive(event, event.getBlock().getLocation(), "DENY@BLOCKS");
    }

    public void cancelIfActive(Cancellable event, Location location, String metadataId) {
        if (isMetadataActive(location, metadataId)) event.setCancelled(true);
    }

    public boolean isMetadataActive(Location location, String metadataId) {
        CuboidRegion region = RegionUtils.getFirstRegionByLocation(location);
        if (region == null) return false;
        return region.getDefaultConfig().hasMetaData(metadataId);
    }

}

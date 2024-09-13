package us.smartmc.lobbymodule.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.core.SmartCore;
import us.smartmc.core.handler.AdminModeHandler;

public class CancelListeners implements Listener {

    private static final AdminModeHandler adminHandler = SmartCore.getPlugin().getAdminModeHandler();

    @EventHandler
    public void damage(BlockDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void blockExplode(ExplosionPrimeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void blockExplode(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void blockExplode(BlockIgniteEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void cancelInteract(PlayerInteractEvent event) {
        if (adminHandler.isActive(event.getPlayer())) return;
        event.setCancelled(true);
    }
}

package us.smartmc.core.listener;

import us.smartmc.core.SmartCore;
import us.smartmc.core.handler.LobbyHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LobbyHandlerListeners implements Listener {

    private final LobbyHandler handler;

    public LobbyHandlerListeners(LobbyHandler handler) {
        this.handler = handler;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void disableJoinMessage(PlayerJoinEvent event) {
        if (handler.isDisabled("disableJoinAndQuitMessages")) return;
        event.setJoinMessage(null);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void enableQuitMessage(PlayerQuitEvent event) {
        if (handler.isDisabled("disableJoinAndQuitMessages")) return;
        event.setQuitMessage(null);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void setDefaultGameModeJoin(PlayerJoinEvent event) {
        if (handler.isDisabled("defaultGameModeEnabled")) return;
        GameMode gameMode = GameMode.valueOf(handler.getConfig().getString("defaultGameMode"));
        Bukkit.getScheduler().runTaskLater(SmartCore.getPlugin(), () -> {
            event.getPlayer().setGameMode(gameMode);
        }, 5);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if (handler.isDisabled("cancelDamage")) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelDamage(FoodLevelChangeEvent event) {
        if (handler.isDisabled("cancelDamage")) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void cancelBlockPlace(BlockPlaceEvent event) {
        if (handler.isDisabled("cancelBlockEvents")) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void cancelBlockBreak(BlockBreakEvent event) {
        if (handler.isDisabled("cancelBlockEvents")) return;
        event.setCancelled(true);
    }

}

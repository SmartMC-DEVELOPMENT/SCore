package us.smartmc.game.luckytowers.listener;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import us.smartmc.core.SmartCore;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.game.GameSessionStatus;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.instance.player.PlayerStatus;
import us.smartmc.game.luckytowers.manager.EditorModeManager;

public class EssentialsListeners implements Listener {

    @EventHandler
    public void gamemode(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTask(LuckyTowers.getPlugin(), () -> {
            try {
                player.setGameMode(GameMode.SURVIVAL);
            } catch (Exception ignore) {}
        });
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void cancelDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());

        if (SmartCore.getPlugin().getAdminModeHandler().isActive(player)) return;

        event.setCancelled(true);

        if (gamePlayer != null && (gamePlayer.getGameSession() != null && gamePlayer.getGameSession().getStatus().equals(GameSessionStatus.PLAYING))) {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void healAtTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setFireTicks(0);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void cancelBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = GamePlayer.get(event.getPlayer().getUniqueId());
        GameSession session = gamePlayer.getGameSession();
        if (SmartCore.getPlugin().getAdminModeHandler().isActive(player)) return;

        event.setCancelled(true);
        if (session != null && session.getStatus().equals(GameSessionStatus.PLAYING)) {
            event.setCancelled(false);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void cancelBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = GamePlayer.get(event.getPlayer().getUniqueId());
        GameSession session = gamePlayer.getGameSession();
        if (SmartCore.getPlugin().getAdminModeHandler().isActive(player)) return;

        event.setCancelled(true);
        if (session != null && session.getStatus().equals(GameSessionStatus.PLAYING)) {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void disableAchievements(PlayerAdvancementDoneEvent event) {
        event.message(null);
    }

    @EventHandler
    public void cancelNaturalSpawns(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Mob || event.getEntity() instanceof Animals)) return;
        if (entity.getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void removeJoinMessage(PlayerJoinEvent event) {
        event.joinMessage(null);
    }

    @EventHandler
    public void removeQuitMessage(PlayerQuitEvent event) {
        event.quitMessage(null);
    }

    @EventHandler
    public void createGamePlayer(PlayerDataLoadedEvent event) {
        GamePlayer.get(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void removeEditorMode(PlayerQuitEvent event) {
        EditorModeManager manager = LuckyTowers.getManager(EditorModeManager.class);
        manager.disable(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW)
    public void cancelDamageIfNotInGame(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        GamePlayer gamePlayer = GamePlayer.get(entity.getUniqueId());
        if (gamePlayer == null) {
            cancel(event);
            return;
        }
        GameSession session = gamePlayer.getGameSession();
        if (session == null) {
            cancel(event);
            return;
        }

        if (gamePlayer.getGameSession().getAlivePlayers().contains(gamePlayer)) {
            event.setCancelled(false);
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void allowInGameDamageFromNotPlayer(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player player)) return;
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        if (gamePlayer == null) return;
        if (gamePlayer.getGameSession() == null) return;

        Entity damager = event.getDamager();
        if (!(damager instanceof Player)) {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void projectileFix(ProjectileHitEvent event) {
        System.out.println("DEBUG PROJECTILE HIT: " + event.getEntity().getClass());
        event.setCancelled(false);
        event.getEntity().getNearbyEntities(0.5, 0.5, 0.5).forEach(entity -> {
            entity.sendMessage("AAA");
        });
    }

    @EventHandler(priority = EventPriority.LOW)
    public void cancelSpectatorDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();

        if (!(damager instanceof Player player)) return;
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());

        if (gamePlayer != null && gamePlayer.getStatus().equals(PlayerStatus.INGAME)
                && (gamePlayer.getGameSession() != null && gamePlayer.getGameSession().getStatus().equals(GameSessionStatus.PLAYING))) {
            event.setCancelled(false);
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void allowFish(PlayerFishEvent event) {
        event.setCancelled(false);
    }

    private static void cancel(Cancellable e) {
        e.setCancelled(true);
    }

}

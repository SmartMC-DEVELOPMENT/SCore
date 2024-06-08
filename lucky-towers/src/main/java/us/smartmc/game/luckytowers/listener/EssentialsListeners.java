package us.smartmc.game.luckytowers.listener;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
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

    @EventHandler(priority = EventPriority.HIGH)
    public void cancelDamageIfNotInGame(EntityDamageEvent event) {
        System.out.println("cancelDamageIfNotInGame: start");
        Entity entity = event.getEntity();
        GamePlayer gamePlayer = GamePlayer.get(entity.getUniqueId());

        if (gamePlayer != null && (gamePlayer.getGameSession() != null && gamePlayer.getGameSession().getAlivePlayers().contains(gamePlayer))) {
            event.setCancelled(false);
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void allowInGameDamageFromNotPlayer(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();

        System.out.println("allowInGameDamageFromNotPlayer: start");
        if (!(entity instanceof Player player)) return;
        System.out.println("allowInGameDamageFromNotPlayer: is player");
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        if (gamePlayer == null) return;
        System.out.println("allowInGameDamageFromNotPlayer: gameplayer not null");
        if (gamePlayer.getGameSession() == null) return;
        System.out.println("allowInGameDamageFromNotPlayer: gamesession not null");

        Entity damager = event.getDamager();
        if (!(damager instanceof Player)) {
            System.out.println("allowInGameDamageFromNotPlayer: damager not player");
            event.setCancelled(false);
            System.out.println("allowInGameDamageFromNotPlayer: set cancelled false");
            player.damage((double) 0, DamageSource.builder(DamageType.ARROW).build());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void cancelSpectatorDamage(EntityDamageByEntityEvent event) {
        System.out.println("cancelSpectatorDamage: start");
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
}

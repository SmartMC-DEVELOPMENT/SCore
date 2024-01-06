package us.smartmc.snowgames.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.core.SmartCore;
import us.smartmc.gamesmanager.player.GamePlayer;
import us.smartmc.gamesmanager.player.GamePlayerRepository;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.game.FFAGame;
import us.smartmc.snowgames.game.FFAMap;
import us.smartmc.snowgames.inventory.LobbyHotbar;
import us.smartmc.snowgames.manager.ItemCooldownManager;
import us.smartmc.snowgames.player.FFAPlayer;
import us.smartmc.snowgames.util.PlayerUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerListeners implements Listener {

    // KEY = VICTIM & VALUE = ATTACKER
    public static Map<UUID, UUID> getAttacked = new HashMap<>();

    @EventHandler
    public void cancelDropItem(PlayerDropItemEvent event) {
        if (SmartCore.getPlugin().getAdminModeHandler().isActive(event.getPlayer())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void giveLobbyHotbar(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LobbyHotbar.give(player);
        player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        FFAPlayer ffaPlayer = GamePlayerRepository.provide(FFAPlayer.class, player);
        if (ffaPlayer == null) return;
        ffaPlayer.saveStats();

        GamePlayer gamePlayer = GamePlayerRepository.provide(FFAPlayer.class, player);
        if (gamePlayer == null) return;
        FFAPlugin.getGame().quitPlayer(gamePlayer);
        GamePlayerRepository.remove(player.getUniqueId());
        ItemCooldownManager.clear(player);

        event.setQuitMessage(null);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().clear();
        event.setDeathMessage("");
        event.setKeepInventory(false);
        FFAPlayer ffaPlayer = GamePlayerRepository.provide(FFAPlayer.class, event.getEntity());
        if (ffaPlayer == null) return;
        FFAPlugin.getGame().deathPlayer(ffaPlayer);
    }

    @EventHandler
    public void damageInSpawn(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        FFAGame game = FFAPlugin.getGame();
        if (game.isInGame(player)) return;
        event.setCancelled(true);
    }


    @EventHandler
    public void killPlayerUnderY20(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        FFAGame game = FFAPlugin.getGame();
        if (!game.isInGame(player)) return;
        FFAMap map = game.getMap();

        Location location = player.getLocation();
        double playerY = location.getBlockY();
        if (playerY < map.getDeathYLocation()) {
            PlayerUtil.kill(player);
        }
    }

    @EventHandler
    public void addKillerToMap(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) return;
        if (!(event.getEntity() instanceof Player victim)) return;
        getAttacked.put(victim.getUniqueId(), attacker.getUniqueId());
        Bukkit.getScheduler().runTaskLater(FFAPlugin.getPlugin(), () -> {
            getAttacked.remove(victim.getUniqueId());
        }, 20 * 10);
    }
}

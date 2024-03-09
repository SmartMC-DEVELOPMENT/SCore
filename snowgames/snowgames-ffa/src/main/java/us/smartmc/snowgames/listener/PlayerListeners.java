package us.smartmc.snowgames.listener;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.event.PlayerLanguageChangedEvent;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.menu.ICoreMenu;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.core.SmartCore;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.player.GamePlayerUnloadEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GamePlayerManager;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.game.FFAGame;
import us.smartmc.snowgames.game.FFAMap;
import us.smartmc.snowgames.inventory.GameHotbar;
import us.smartmc.snowgames.inventory.LobbyHotbar;
import us.smartmc.snowgames.manager.FFAPlayerManager;
import us.smartmc.snowgames.manager.ItemCooldownManager;
import us.smartmc.snowgames.menu.FFAMenu;
import us.smartmc.snowgames.player.FFAPlayer;
import us.smartmc.snowgames.util.PlayerUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerListeners implements Listener {

    // KEY = VICTIM & VALUE = ATTACKER
    public static Map<UUID, UUID> getAttacked = new HashMap<>();

    @EventHandler
    public void changeLanguage(PlayerLanguageChangedEvent event) {
        Player player = event.getCorePlayer().get();
        ICoreMenu menu = SmartCorePlayer.get(player).getCurrentMenuSet();
        if (!(menu instanceof FFAMenu)) return;
        FFAMenu ffaMenu = (FFAMenu) menu;

        // If in game not give any new menu (Not update language, evitar dupes)
        if (FFAPlugin.getGame().isInGame(player)) return;

        FFAMenu.give(player, ffaMenu.getClass());
    }

    @EventHandler
    public void registerGamePlayer(PlayerJoinEvent event) {
        GamePlayerManager<FFAPlayer> manager = FFAPlugin.getFFAPlugin().getGamePlayerManager();
        manager.register(new FFAPlayer(manager, event.getPlayer()));
    }

    @EventHandler
    public void cancelDropItem(PlayerDropItemEvent event) {
        if (SmartCore.getPlugin().getAdminModeHandler().isActive(event.getPlayer())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void giveLobbyHotbar(PlayerDataLoadedEvent event) {
        Player player = event.getPlayer();
        FFAMenu.give(player, LobbyHotbar.class);
        player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    public void onUnloadGamePlayer(GamePlayerUnloadEvent event) {
        GamePlayer gamePlayer = event.getGamePlayer();
        FFAPlayer ffaPlayer = FFAPlugin.getFFAPlugin().getGamePlayerManager().get(gamePlayer.getUUID());
        if (ffaPlayer == null) return;
        FFAPlugin.getGame().quitPlayer(ffaPlayer);
        ItemCooldownManager.clear(gamePlayer.getPlayer());
    }

    @EventHandler
    public void removeJoinMessages(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        event.setDeathMessage(null);
        FFAPlayer ffaPlayer = FFAPlugin.getFFAPlugin().getGamePlayerManager().get(event.getEntity().getUniqueId());
        if (ffaPlayer == null) return;
        PlayerUtil.kill(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void damageInSpawn(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        FFAGame game = FFAPlugin.getGame();
        if (game.isInGame(FFAPlugin.getFFAPlugin().getGamePlayerManager().get(player.getUniqueId()))) return;
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
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;
        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();
        getAttacked.put(victim.getUniqueId(), attacker.getUniqueId());
        Bukkit.getScheduler().runTaskLater(FFAPlugin.getFFAPlugin(), () -> {
            getAttacked.remove(victim.getUniqueId());
        }, 20 * 10);
    }
}

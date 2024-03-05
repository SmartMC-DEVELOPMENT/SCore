package us.smartmc.snowgames.listener;

import me.imsergioh.pluginsapi.event.PlayerTickEvent;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.game.FFAGame;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static us.smartmc.snowgames.config.DefaultConfig.getJoinMessage;
import static us.smartmc.snowgames.config.DefaultConfig.isJoinMessageEnabled;

public class GameListeners implements Listener {

    private static final Set<UUID> recentJoined = new HashSet<>();

    public GameListeners() {
        CorePlayer.setEnabledTickEvent(true);
    }

    @EventHandler
    public void cancelEntitySpawn(EntitySpawnEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void cancelItemSpawn(ItemSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void changeWeather(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void fallDamage(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) event.setCancelled(true);
    }

    @EventHandler
    public void joinGameAtLeaveSpawn(PlayerTickEvent event) {
        Player player = event.getCorePlayer().get();
        if (event.getCorePlayer() == null) return;
        if (player == null) return;
        GamePlayer gamePlayer = FFAPlugin.getFFAPlugin().getGamePlayerManager().get(player.getUniqueId());
        if (gamePlayer == null) return;
        FFAGame game = FFAPlugin.getGame();
        if (game == null) return;
        boolean inGame = game.isInGame(gamePlayer);
        boolean atSpawn = player.getLocation().getY() >= FFAPlugin.getGame().getMap().getSpawnYLocation();

        if (!inGame && !atSpawn && !recentJoined.contains(player.getUniqueId())) {
            game.joinPlayer(gamePlayer);
        }
    }

    public boolean hasItemsInInventory(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void JoinServerMessage(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        recentJoined.add(player.getUniqueId());
        player.setHealth(20);
        player.setFoodLevel(20);

        if (isJoinMessageEnabled()) {
            event.setJoinMessage(ChatUtil.parse(event.getPlayer(), getJoinMessage(), event.getPlayer().getDisplayName()));
        } else {
            event.setJoinMessage(null);
        }
        Location spawn = FFAPlugin.getGame().getSpawn();
        if (spawn == null) return;
        player.teleport(spawn);

        Bukkit.getScheduler().runTaskLater(FFAPlugin.getFFAPlugin(), () -> {
            recentJoined.remove(player.getUniqueId());
        }, 10);
    }

    @EventHandler
    public void fixFood(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}

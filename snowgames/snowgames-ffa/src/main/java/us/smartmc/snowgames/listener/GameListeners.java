package us.smartmc.snowgames.listener;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import us.smartmc.gamesmanager.player.GamePlayer;
import us.smartmc.gamesmanager.player.GamePlayerRepository;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.game.FFAGame;
import us.smartmc.snowgames.inventory.LobbyHotbar;
import us.smartmc.snowgames.player.FFAPlayer;
import us.smartmc.snowgames.util.RegionUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static us.smartmc.snowgames.config.DefaultConfig.getJoinMessage;
import static us.smartmc.snowgames.config.DefaultConfig.isJoinMessageEnabled;

public class GameListeners implements Listener {

    public static Set<UUID> teleportingSpawn = new HashSet<>();

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
    public void blockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        FFAGame game = FFAPlugin.getGame();
        if (!game.isInGame(player)) return;
        if (RegionUtils.isAtSpawn(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void joinGameAtLeaveSpawn(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        FFAGame game = FFAPlugin.getGame();

        if (!game.isInGame(player) && !RegionUtils.isAtSpawn(player)) {

            GamePlayer gamePlayer = GamePlayerRepository.provide(FFAPlayer.class, player);
            if (gamePlayer == null) return;

            if (teleportingSpawn.contains(player.getUniqueId())) return;
            game.joinPlayer(gamePlayer);
            return;
        }

        if (RegionUtils.isAtSpawn(player)) {

            teleportingSpawn.remove(player.getUniqueId());

            GamePlayer gamePlayer = GamePlayerRepository.provide(FFAPlayer.class, player);
            if (gamePlayer == null) return;
            if (game.isInGame(player)) game.quitPlayer(gamePlayer);
            if (!hasItemsInInventory(player)) {
                LobbyHotbar.give(player);
            }
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

    @EventHandler
    public void JoinServerMessage(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setHealth(20);
        player.setFoodLevel(20);

        if (isJoinMessageEnabled()) {
            event.setJoinMessage(ChatUtil.parse(event.getPlayer(), getJoinMessage(), event.getPlayer().getDisplayName()));
        } else {
            event.setJoinMessage(null);
        }
    }

    @EventHandler
    public void fixFood(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}

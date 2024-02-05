package us.smartmc.snowgames.listener;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.game.FFAGame;
import us.smartmc.snowgames.manager.FFAPlayerManager;
import us.smartmc.snowgames.player.FFAPlayer;
import us.smartmc.snowgames.util.RegionUtils;

import static us.smartmc.snowgames.config.DefaultConfig.getJoinMessage;
import static us.smartmc.snowgames.config.DefaultConfig.isJoinMessageEnabled;

public class GameListeners implements Listener {

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
    public void joinGameAtLeaveSpawn(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        FFAGame game = FFAPlugin.getGame();
        boolean inGame = game.isInGame(player);
        boolean atSpawn = RegionUtils.isAtSpawn(player);

        if (!inGame && !atSpawn) {
            FFAPlayer gamePlayer = FFAPlayerManager.INSTANCE.get(player.getUniqueId());
            if (gamePlayer == null) return;
            game.joinPlayer(player);
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
        player.teleport(FFAPlugin.getGame().getSpawn());
    }

    @EventHandler
    public void fixFood(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }
}

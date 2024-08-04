package us.smartmc.game.luckytowers.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.command.LeaveCommand;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.game.GameSessionStatus;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.instance.player.PlayerStatus;
import us.smartmc.game.luckytowers.util.GameUtil;

public class MainGameListeners implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void cancelPickItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());

        if (gamePlayer == null) {
            GameUtil.cancel(event);
            return;
        }

        if (gamePlayer.getStatus().equals(PlayerStatus.INGAME)) {
            event.setCancelled(false);
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void cancelDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        if (gamePlayer == null) {
            GameUtil.cancel(event);
            return;
        }

        if (gamePlayer.getStatus().equals(PlayerStatus.INGAME) &&
                gamePlayer.getGameSession().getStatus().equals(GameSessionStatus.PLAYING)) {
            event.setCancelled(false);
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void quit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        if (gamePlayer == null) return;
        GameSession session = gamePlayer.getGameSession();
        if (session == null) return;
        session.quitPlayer(gamePlayer);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void death(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        Player player = event.getEntity();
        Location deathLocation = player.getLocation();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        GameSession session = gamePlayer.getGameSession();
        if (session == null) return;
        //event.setCancelled(true);
        Bukkit.getScheduler().runTask(LuckyTowers.getPlugin(), () -> {
            session.deathPlayer(gamePlayer);
        });

        // Add kill to killer if not null
        Player killer = player.getKiller();
        if (killer != null) {
            GamePlayer.get(killer.getUniqueId()).addKill(player.getLocation());
        }

        // Calls method deathPlayer & checks if disconnected leave from game

        Bukkit.getScheduler().runTaskLater(LuckyTowers.getPlugin(), () -> {
            if (player.isOnline()) return;
            LeaveCommand.leave(player);
        }, 2);
    }
}

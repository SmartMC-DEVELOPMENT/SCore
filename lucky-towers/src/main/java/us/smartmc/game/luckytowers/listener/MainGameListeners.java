package us.smartmc.game.luckytowers.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.command.LeaveCommand;
import us.smartmc.game.luckytowers.instance.game.GameSession;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;

public class MainGameListeners implements Listener {

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        player.damage(999);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void death(PlayerDeathEvent event) {
        event.deathMessage(null);
        Player player = event.getPlayer();
        GamePlayer gamePlayer = GamePlayer.get(player.getUniqueId());
        GameSession session = gamePlayer.getGameSession();
        if (session == null) return;
        event.setCancelled(true);

        // Add kill to killer if not null
        Player killer = player.getKiller();
        if (killer != null) {
            GamePlayer.get(killer.getUniqueId()).addKill(player.getLocation());
        }

        // Calls method deathPlayer & checks if disconnected leave from game
        session.deathPlayer(gamePlayer);
        Bukkit.getScheduler().runTaskLater(LuckyTowers.getPlugin(), () -> {
            if (player.isOnline()) return;
            LeaveCommand.leave(player);
        }, 2);
    }
}

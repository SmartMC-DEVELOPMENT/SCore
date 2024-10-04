package us.smartmc.gamescore.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.event.player.GamePlayerQuitEvent;
import us.smartmc.gamescore.instance.game.Game;
import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.instance.player.PlayerStatus;

public class PlayerGameLogicListeners implements Listener {

    @EventHandler
    public void checkDamageSameGame(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player damager && event.getEntity() instanceof Player entity)) return;
        GameCorePlayer damagerGamePlayer = GameCorePlayer.of(damager);
        GameCorePlayer entityGamePlayer = GameCorePlayer.of(entity);
        Game g1 = damagerGamePlayer.getCurrentGame();
        Game g2 = entityGamePlayer.getCurrentGame();
        if ((g1 == null || g2 == null) || (g1 != g2)) {
            event.setCancelled(true);
        } else {
            event.setCancelled(false);
        }
    }

    @EventHandler
    public void onPlayerQuit(GamePlayerQuitEvent event) {
        GamesCoreAPI.getGamesManager().unregisterPlayer(event.getBukkitPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void cancelDamageIfNotInGame(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            GameCorePlayer gamePlayer = GameCorePlayer.of(player);
            if (gamePlayer == null) return;
            if (gamePlayer.getStatus().equals(PlayerStatus.IN_GAME)) {
                event.setCancelled(false);
                return;
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        GameCorePlayer gamePlayer = GameCorePlayer.of(player);
        Game game = gamePlayer.getCurrentGame();
        if (game == null) return;
        game.killPlayer(gamePlayer);
    }
}

package us.smartmc.snowgames.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import us.smartmc.gamesmanager.player.GamePlayerRepository;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.game.FFAGame;
import us.smartmc.snowgames.player.FFAPlayer;

public class DamageListeners implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void cancelFallDamageIfNotInGame(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;
        FFAGame game = FFAPlugin.getGame();
        if (game.isInGame(player)) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void joinAtFallDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) return;
        FFAGame game = FFAPlugin.getGame();
        if (game.isInGame(player)) return;
        FFAPlayer ffaPlayer = GamePlayerRepository.provide(FFAPlayer.class, player);

        game.joinPlayer(ffaPlayer);
    }

    @EventHandler
    public void killByVoidDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.VOID)) return;
        player.damage(999);
        event.setCancelled(true);
    }

    @EventHandler
    public void changeDamageFromSnowball(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();

        if (damager instanceof Snowball) {
            event.setDamage(1.5D);
        }
    }

}

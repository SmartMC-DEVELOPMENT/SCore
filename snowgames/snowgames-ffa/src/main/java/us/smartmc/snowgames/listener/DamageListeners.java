package us.smartmc.snowgames.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListeners implements Listener {

    /*
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
     */

    @EventHandler
    public void changeDamageFromSnowball(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();

        if (damager instanceof Snowball) {
            event.setDamage(1.5D);
        }
    }

}

package us.smartmc.snowgames.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import static us.smartmc.snowgames.listener.PlayerListeners.getAttacked;

public class SnowBallDamageListener implements Listener {

    @EventHandler
    public void changeDamageFromSnowball(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof Snowball snowball) {
            event.setDamage(1.5D);
            ProjectileSource shooter = snowball.getShooter();

            if (shooter == null) return;
            if (!(shooter instanceof Player shooterPlayer)) return;
            getAttacked.put(event.getEntity().getUniqueId(), shooterPlayer.getUniqueId());
        }
    }

}

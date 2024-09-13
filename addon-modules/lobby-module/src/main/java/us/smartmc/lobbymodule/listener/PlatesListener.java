package us.smartmc.lobbymodule.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import us.smartmc.core.SmartCore;
import us.smartmc.smartaddons.plugin.AddonListener;

import java.util.*;

public class PlatesListener extends AddonListener implements Listener {

    private static final Set<UUID> cooldowns = new HashSet<>();

    @EventHandler
    public void impulsePlayer(PlayerInteractEvent event) {
        if (!isEnabled()) return;
        if (!event.getAction().equals(Action.PHYSICAL)) return;
        if (!event.getClickedBlock().getType().equals(Material.IRON_PLATE)) return;
        Player player = event.getPlayer();

        // Obtén la dirección en la que está mirando el jugador
        Vector direction = player.getLocation().getDirection().normalize();

        // Aplica un impulso controlado en la dirección que el jugador mira
        Vector impulse = direction.multiply(0.5).setZ(3.2).setY(1); // Multiplica la dirección y añade un impulso vertical

        // Aplica el impulso al jugador
        player.setVelocity(impulse);

        // Aplica una corrección después de un pequeño retraso para asegurar que la física del juego no corrija el impulso
        new BukkitRunnable() {
            @Override
            public void run() {
                Vector correctionImpulse = direction.multiply(0.5).setZ(3.2).setY(1);
                player.setVelocity(correctionImpulse);
            }
        }.runTaskLater(SmartCore.getPlugin(), 1L); // 1 tick de retardo
    }

}

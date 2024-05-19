package us.smartmc.lobbymodule.util;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import us.smartmc.core.SmartCore;

import java.util.List;

public class FireworkUtil {

    public static void spawnCustomFirework(Player player, List<Color> colors, List<Color> fadeColors,
                                           FireworkEffect.Type type, boolean flicker, boolean trail,
                                           int power, long delay) {
        Location location = player.getLocation();
        Firework firework = player.getWorld().spawn(location, Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        FireworkEffect.Builder builder = FireworkEffect.builder();

        if (colors != null && !colors.isEmpty()) {
            builder.withColor(colors);
        }

        if (fadeColors != null && !fadeColors.isEmpty()) {
            builder.withFade(fadeColors);
        }

        if (type != null) {
            builder.with(type);
        }

        builder.flicker(flicker);
        builder.trail(trail);

        fireworkMeta.addEffect(builder.build());
        fireworkMeta.setPower(power);
        firework.setFireworkMeta(fireworkMeta);

        // Detonate the firework after specified delay
        new BukkitRunnable() {
            @Override
            public void run() {
                firework.detonate();
            }
        }.runTaskLater(SmartCore.getPlugin(), delay);
    }

}

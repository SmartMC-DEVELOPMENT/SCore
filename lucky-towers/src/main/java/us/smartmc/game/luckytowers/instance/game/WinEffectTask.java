package us.smartmc.game.luckytowers.instance.game;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;

public class WinEffectTask {

    private static final LuckyTowers plugin = LuckyTowers.getPlugin();
    private static final int DEFAULT_COOLDOWN = 5;

    private final GameSession session;
    private final GamePlayer winner;

    private final Player winnerBukkit;

    private int cooldown = DEFAULT_COOLDOWN;
    private final BukkitRunnable runnable;

    public WinEffectTask(GameSession session, GamePlayer winner) {
        this.session = session;
        this.winner = winner;
        this.winnerBukkit = winner.getBukkitPlayer();
        this.runnable = getDefaultRunnable();
    }

    public void start() {
        runnable.runTaskTimer(plugin, 0, 20);
    }

    public void repeatingTaskAction() {
        if (winnerBukkit == null || !winnerBukkit.isOnline()) return;
        Location location = winnerBukkit.getLocation().add(0, 2, 0);
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.addEffect(FireworkEffect.builder().flicker(true).withFade(Color.BLUE).withColor(Color.AQUA).trail(true).build());
        meta.setPower(10);

        firework.setFireworkMeta(meta);
        firework.detonate();
    }

    public void end() {
        session.resetAndEndMap();
    }

    private BukkitRunnable getDefaultRunnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (cooldown > 0) {
                    repeatingTaskAction();
                } else {
                    end();
                    cancel();
                }
                cooldown--;
            }
        };
    }

}

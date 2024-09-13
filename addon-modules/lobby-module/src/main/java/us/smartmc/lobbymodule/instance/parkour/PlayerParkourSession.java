package us.smartmc.lobbymodule.instance.parkour;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.instance.player.CorePlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.smartmc.core.SmartCore;
import us.smartmc.core.instance.player.SmartCorePlayer;

import java.util.HashMap;
import java.util.UUID;

public class PlayerParkourSession {

    private static final String DATABASE_KEY = "lobby.parkour.bestTime";

    private static final HashMap<UUID, PlayerParkourSession> sessions = new HashMap<>();

    @Getter
    private final Player player;

    private long startMillis, endMillis;
    private final boolean wasFlying;
    private boolean finish;
    private BukkitRunnable runnable;

    public PlayerParkourSession(Player player) {
        this.player = player;
        this.wasFlying = player.isFlying();
        sessions.put(player.getUniqueId(), this);
    }

    public boolean hasReachedNewRecord() {
        double diff = getDiffInSeconds();
        if (getDatabaseElapsedTime() > diff) {
            CorePlayer.get(player).getPlayerData().set(DATABASE_KEY, diff);
            player.sendMessage(CorePlayer.get(player).getPlayerData().getDataMap().toString());
            return true;
        }
        return false;
    }

    public double getDatabaseElapsedTime() {
        CorePlayer corePlayer = CorePlayer.get(player);
        if (corePlayer == null) return 99999;
        CorePlayerData data = corePlayer.getPlayerData();
        if (data == null) return 99999;
        if (data.containsKey(DATABASE_KEY))
            return data.get(DATABASE_KEY, Double.class);
        return 99999;
    }

    public void cancelTask() {
        if (runnable == null) return;
        Bukkit.getScheduler().cancelTask(runnable.getTaskId());
        runnable.cancel();
    }

    public void cancel() {
        if (runnable != null) {
            endMillis = System.currentTimeMillis();
            cancelTask();
            if (wasFlying) {
                player.setAllowFlight(true);
            }
        }
    }

    public double getDiffInSeconds() {
        long diff = endMillis - startMillis;
        return diff / 1000.0;
    }

    public void registerStart() {
        startMillis = System.currentTimeMillis();
        player.setAllowFlight(false);
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (finish) {
                    this.cancel();
                    return;
                }

                try {
                    long currentMillis = System.currentTimeMillis() - startMillis;
                    double seconds = currentMillis / 1000.0;
                    String formattedTime = String.format("%.2f", seconds);
                    SmartCorePlayer.get(player).sendActionBar("&a" + formattedTime + "s");
                } catch (Exception e) {
                    this.cancel();
                }
            }
        };
        runnable.runTaskTimer(SmartCore.getPlugin(), 0, 2);
    }

    public void registerEnd() {
        Bukkit.getPluginManager().callEvent(new PlayerParkourEndedEvent(this));
        finish = true;
        endMillis = System.currentTimeMillis();
        cancel();
    }

    public static boolean isActive(Player player) {
        return sessions.containsKey(player.getUniqueId());
    }

    public static PlayerParkourSession getSession(Player player, boolean create) {
        if (sessions.containsKey(player.getUniqueId())) return sessions.get(player.getUniqueId());
        if (create)
            return new PlayerParkourSession(player);
        else return null;
    }

    public static void remove(Player player) {
        PlayerParkourSession session = sessions.remove(player.getUniqueId());
        if (session != null)
            session.cancelTask();
    }

}

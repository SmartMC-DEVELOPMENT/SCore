package us.smartmc.lobbymodule.instance.parkour;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.instance.player.CorePlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

    private ItemStack[] oldInventoryContent;

    @Getter
    private Location exitLocation;

    public PlayerParkourSession(Player player) {
        this.player = player;
        this.wasFlying = player.getAllowFlight();
        sessions.put(player.getUniqueId(), this);
        exitLocation = player.getLocation().add(0, 0, -5);
    }

    public boolean hasReachedNewRecord() {
        long diff = getDiffMillis();

        if (getDatabaseElapsedTime() > diff) {
            CorePlayer.get(player).getPlayerData().set(DATABASE_KEY, diff);
            return true;
        }
        return false;
    }

    public long getDatabaseElapsedTime() {
        CorePlayer corePlayer = CorePlayer.get(player);
        CorePlayerData data = corePlayer.getPlayerData();
        try {
            return data.get(DATABASE_KEY, Number.class).longValue();
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    public void cancelTask() {
        if (runnable == null) return;
        Bukkit.getScheduler().cancelTask(runnable.getTaskId());
        runnable.cancel();
        if (wasFlying) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
        Bukkit.getScheduler().runTask(SmartCore.getPlugin(), () -> {
            player.getInventory().clear();
            player.getInventory().setContents(oldInventoryContent);
        });
    }

    public void cancel() {
        if (runnable != null) {
            endMillis = System.currentTimeMillis();
            cancelTask();
        }
    }

    public long getDiffMillis() {
        return endMillis - startMillis;
    }

    public void registerStart() {
        startMillis = System.currentTimeMillis();
        player.setAllowFlight(false);
        player.setFlying(false);
        oldInventoryContent = player.getInventory().getContents();
        player.getInventory().clear();
        player.getInventory().setItem(8, ItemBuilder.of(Material.BARRIER).name("<lang.lobby.exit_parkour_name>").get());
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
        endMillis = System.currentTimeMillis() - 100;
        finish = true;
        Bukkit.getPluginManager().callEvent(new PlayerParkourEndedEvent(this));
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
        remove(player, false);
    }

    public static void remove(Player player, boolean exit) {
        PlayerParkourSession session = sessions.remove(player.getUniqueId());
        if (exit) {
            player.teleport(session.getExitLocation());
        }

        if (session != null)
            session.cancelTask();
    }

}

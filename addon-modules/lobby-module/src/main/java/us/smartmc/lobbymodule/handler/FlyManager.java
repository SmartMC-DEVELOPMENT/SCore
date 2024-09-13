package us.smartmc.lobbymodule.handler;

import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import me.imsergioh.pluginsapi.instance.player.CorePlayerData;
import org.bson.Document;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import us.smartmc.smartaddons.plugin.AddonListener;

import java.util.Timer;

public class FlyManager extends AddonListener implements Listener {

    public static final String FLY_PERMISSION = "smartmc.vip.fly";
    public static final String DATA_PATH = "fly";

    @EventHandler
    public void loadedPlayerData(PlayerDataLoadedEvent event) {
        if (!isEnabled()) return;
        CorePlayer corePlayer = event.getCorePlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (canFly(corePlayer)) {
                    event.getData().register(DATA_PATH, true);
                } else {
                    event.getData().remove(DATA_PATH);
                    return;
                }

                boolean flying = isFlyingEnabled(corePlayer);
                toggle(corePlayer, flying);
            }
        }.runTaskLater(SpigotPluginsAPI.getPlugin(), 5);
    }

    public static void toggle(CorePlayer corePlayer, boolean active) {
        if (active) {
            corePlayer.getPlayerData().set(DATA_PATH, true);
            if (!corePlayer.get().isFlying()) {
                corePlayer.get().setAllowFlight(true);
                corePlayer.get().setFlying(true);
            }
            return;
        }

        corePlayer.getPlayerData().set(DATA_PATH, false);
        if (corePlayer.get().isFlying()) {
            corePlayer.get().setAllowFlight(false);
            corePlayer.get().setFlying(false);
        }
    }

    public static boolean isFlyingEnabled(CorePlayer corePlayer) {
        CorePlayerData data = corePlayer.getPlayerData();
        if (data.containsKey(DATA_PATH)) {
            return corePlayer.getPlayerData().get(DATA_PATH, Boolean.class);
        }
        return false;
    }

    public static boolean canFly(CorePlayer corePlayer) {
        if (corePlayer.get() == null) return false;
        return corePlayer.get().hasPermission(FLY_PERMISSION);
    }
}

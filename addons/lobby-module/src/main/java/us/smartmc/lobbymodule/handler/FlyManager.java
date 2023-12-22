package us.smartmc.lobbymodule.handler;

import us.smartmc.core.pluginsapi.spigot.event.PlayerDataLoadedEvent;

import us.smartmc.core.pluginsapi.spigot.player.CorePlayer;
import us.smartmc.core.pluginsapi.util.SyncUtil;
import org.bson.Document;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.smartmc.smartaddons.plugin.AddonListener;

public class FlyManager extends AddonListener implements Listener {

    public static final String FLY_PERMISSION = "smartmc.vip.fly";
    public static final String DATA_PATH = "fly";

    @EventHandler
    public void loadedPlayerData(PlayerDataLoadedEvent event) {
        if (!isEnabled()) return;
        CorePlayer corePlayer = event.getCorePlayer();

        SyncUtil.later(() -> {
            if (canFly(corePlayer)) {
                event.getData().registerData(DATA_PATH, true);
            } else {
                event.getData().getDocument().remove(DATA_PATH);
                return;
            }

            boolean flying = isFlyingEnabled(corePlayer);
            toggle(corePlayer, flying);
        }, 250);
    }

    public static void toggle(CorePlayer corePlayer, boolean active) {
        if (active) {
            corePlayer.getPlayerData().setData(DATA_PATH, true);
            if (!corePlayer.get().isFlying()) {
                corePlayer.get().setAllowFlight(true);
                corePlayer.get().setFlying(true);
            }
            return;
        }

        corePlayer.getPlayerData().setData(DATA_PATH, false);
        if (corePlayer.get().isFlying()) {
            corePlayer.get().setAllowFlight(false);
            corePlayer.get().setFlying(false);
        }
    }

    public static boolean isFlyingEnabled(CorePlayer corePlayer) {
        Document document = corePlayer.getPlayerData().getDocument();
        if (document.containsKey(DATA_PATH)) {
            return corePlayer.getPlayerData().getDocument().getBoolean(DATA_PATH);
        }
        return false;
    }

    public static boolean canFly(CorePlayer corePlayer) {
        if (corePlayer.get() == null) return false;
        return corePlayer.get().hasPermission(FLY_PERMISSION);
    }
}

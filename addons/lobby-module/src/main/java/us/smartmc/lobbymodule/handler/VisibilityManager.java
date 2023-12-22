package us.smartmc.lobbymodule.handler;

import us.smartmc.core.SmartCore;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.core.pluginsapi.spigot.event.PlayerDataLoadedEvent;
import us.smartmc.core.pluginsapi.spigot.player.CorePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.smartmc.lobbymodule.instance.PlayerVisibility;
import us.smartmc.smartaddons.plugin.AddonListener;
import us.smartmc.smartaddons.spigot.SmartAddonsSpigot;

public class VisibilityManager extends AddonListener implements Listener {

    private static final String VIP_PERMISSIONS = "smartmc.vip";

    @EventHandler
    public void onJoin(PlayerDataLoadedEvent event) {
        if (!isEnabled()) return;
        Bukkit.getScheduler().runTaskLater(SmartCore.getPlugin(), () -> {
            Bukkit.getOnlinePlayers().forEach(VisibilityManager::update);
        }, 2);
    }

    public static void update(Player player) {
        SmartCorePlayer corePlayer = SmartCorePlayer.get(player);

        String visibilityName = corePlayer.getPlayerData().getDocument().get("visibility", String.class);

        if (visibilityName == null) visibilityName = PlayerVisibility.DEFAULT.toString();

        PlayerVisibility visibility = PlayerVisibility.valueOf(visibilityName);

        switch (visibility) {
            case VIPS: {
                hideAll(player, VIP_PERMISSIONS);
                break;
            }
            case DEFAULT:
                showAll(player);
                break;
            case NO_ONE:
                hideAll(player, null);
                break;
        }
    }

    private static void hideAll(Player player, String permission) {
        Bukkit.getScheduler().runTaskLater(SmartAddonsSpigot.getPlugin(), () -> {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (player.equals(all)) continue;
                if (permission == null) {
                    player.hidePlayer(all);
                    continue;
                }
                if (!all.hasPermission(permission)) {
                    player.hidePlayer(all);
                }
            }
        },4);

    }

    private static void showAll(Player player) {
        Bukkit.getScheduler().runTaskLater(SmartAddonsSpigot.getPlugin(), () -> {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (player.equals(all)) continue;
                player.showPlayer(all);
            }
        },0);
    }

    public static String getVipPermissions() {
        return VIP_PERMISSIONS;
    }

}

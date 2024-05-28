package us.smartmc.lobbymodule.handler;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.smartmc.core.instance.player.SmartCorePlayer;
import us.smartmc.lobbymodule.instance.PlayerVisibility;
import us.smartmc.smartaddons.plugin.AddonListener;
import us.smartmc.smartaddons.spigot.SmartAddonsSpigot;

import java.util.Arrays;
import java.util.List;

public class VisibilityManager extends AddonListener implements Listener {

    private static final String VIP_PERMISSIONS = "smartmc.vip";

    @EventHandler
    public void onJoin(PlayerDataLoadedEvent event) {
        if (!isEnabled()) return;
        for (Player player : Bukkit.getOnlinePlayers()) {
            update(player);
        }
    }

    public static void set(CorePlayer corePlayer, PlayerVisibility visibility) {
        corePlayer.getPlayerData().getDocument().put("visibility", visibility.name());
        VisibilityManager.update(corePlayer.get());
    }

    public static PlayerVisibility getNext(PlayerVisibility of) {
        int index = 0;
        List<PlayerVisibility> list = Arrays.asList(PlayerVisibility.values());
        for (PlayerVisibility visibility : PlayerVisibility.values()) {
            if (visibility.equals(of)) break;
            index++;
        }
        index++;
        if (index >= list.size()) index = 0;
        return list.get(index);
    }

    public static PlayerVisibility getVisibility(Player player) {
        String visibilityName = CorePlayer.get(player).getPlayerData().getDocument()
                .getString("visibility");
        if (visibilityName == null) visibilityName = PlayerVisibility.DEFAULT.toString();
        return PlayerVisibility.valueOf(visibilityName);
    }

    public static ItemBuilder getVisibilityItem(Player player, PlayerVisibility visibility) {
        Material material;
        switch (visibility) {
            case DEFAULT -> material = Material.LIME_DYE;
            case NO_ONE -> material = Material.GRAY_DYE;
            case VIPS -> material = Material.PINK_DYE;
            default -> material = Material.BARRIER;
        }

        return ItemBuilder
                .of(material)
                .name("<lang.lobby.items.visibility.name>")
                .lore(PlayerVisibility.getOptionItemLore(player));
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

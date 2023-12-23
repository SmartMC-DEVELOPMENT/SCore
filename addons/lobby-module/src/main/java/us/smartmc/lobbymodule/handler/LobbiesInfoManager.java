package us.smartmc.lobbymodule.handler;

import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import us.smartmc.lobbymodule.LobbyModule;
import us.smartmc.lobbymodule.menu.LobbiesMenu;
import us.smartmc.smartaddons.plugin.AddonListener;
import us.smartmc.smartaddons.spigot.SmartAddonsSpigot;

import java.util.HashSet;
import java.util.Set;

public class LobbiesInfoManager extends AddonListener implements Listener {

    private static final Set<LobbiesMenu> menus = new HashSet<>();

    public static void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SmartAddonsSpigot.getPlugin(), () -> {
            if (menus.isEmpty()) return;
            menus.forEach(menu -> {
                // Remove from cache if no viewers or update (load method)
                if (menu.getInventory().getViewers().isEmpty()) {
                    menus.remove(menu);
                } else {
                    menu.load();
                }
            });
        }, 20, 20);
    }

    public static void registerMenu(LobbiesMenu menu) {
        menus.add(menu);
    }

    public static String getIDPrefix() {
        return LobbyModule.getLobbiesMenuConfig().getString("id_prefix");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent event) {
        if (!isEnabled()) return;
        if (!(event.getPlayer() instanceof Player player)) return;
        CorePlayer corePlayer = CorePlayer.get(player);
        CoreMenu menuOpen = corePlayer.getCurrentMenuOpen();
        if (menuOpen == null) return;
        menus.remove(menuOpen);
        System.out.println("Closed menu of instance " + menuOpen.getClass().getName());
    }

}

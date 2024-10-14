package us.smartmc.lobbymodule.handler;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.menu.GUIMenu;;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.core.SmartCore;
import us.smartmc.lobbymodule.LobbyModule;
import us.smartmc.lobbymodule.backend.command.UpdateLobbyInfoCommand;
import us.smartmc.lobbymodule.menu.LobbiesMenu;
import us.smartmc.serverhandler.manager.AbstractCountPusher;
import us.smartmc.smartaddons.plugin.AddonListener;
import us.smartmc.smartaddons.spigot.SmartAddonsSpigot;

import java.util.*;

public class LobbiesInfoManager extends AddonListener implements Listener {

    private static final String LOBBIES_INFO_CONTEXT = "lobby-module:lobbies-info";

    private static final Set<GUIMenu> menus = new HashSet<>();

    @Getter
    private static final TreeMap<String, Document> infos = new TreeMap<>();

    public static void registerDefaults() {
        ConnectionInputManager.registerCommands(new UpdateLobbyInfoCommand());
        SmartCore.getPlugin().getBackendClient().subscribeContext(LOBBIES_INFO_CONTEXT);

        AbstractCountPusher infoPusher = new AbstractCountPusher(count -> {
            broadcastLobbyInfo();
        }, 500, 1000) {
            @Override
            public int getCount() {
                return Bukkit.getOnlinePlayers().size();
            }
        };
        infoPusher.forcePush();
    }

    public static Collection<Document> lobbyInfoModules() {
        return infos.values();
    }

    public static void updateInfo(Document document) {
        String id = document.getString("id");
        infos.put(id, document);
    }

    private static void broadcastLobbyInfo() {
        String serverId = SmartCore.getServerId();
        Document document = new Document("id", serverId);
        document.append("online", Bukkit.getOnlinePlayers().size());
        document.append("max", Bukkit.getMaxPlayers());
        SmartCore.getPlugin().getBackendClient()
                .broadcastCommand(LOBBIES_INFO_CONTEXT,
                "updateLobbyInfo " + document.toJson());
    }

    public static void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SmartAddonsSpigot.getPlugin(), () -> {
            if (menus.isEmpty()) return;
            menus.forEach(menu -> {
                if (menu.getInventory().getViewers().isEmpty()) {
                    menus.remove(menu);
                    return;
                }
                menu.load();
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
        if (corePlayer == null) return;
        GUIMenu menuOpen = GUIMenu.getOpenGUI(player);
        if (menuOpen == null) return;
        menus.remove(menuOpen);
    }
}

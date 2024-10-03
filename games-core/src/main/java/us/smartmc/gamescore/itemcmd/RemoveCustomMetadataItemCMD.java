package us.smartmc.gamescore.itemcmd;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.smartmc.gamescore.api.GamesCoreAPI;
import us.smartmc.gamescore.manager.RegionsManager;
import us.smartmc.gamescore.util.BukkitUtil;

import java.util.*;

public class RemoveCustomMetadataItemCMD implements ItemActionExecutor, Listener {

    private static final Map<UUID, String> toRemoveMap = new HashMap<>();

    public RemoveCustomMetadataItemCMD() {
        Bukkit.getPluginManager().registerEvents(this, GamesCoreAPI.getApi().getPlugin());
    }

    @Override
    public void execute(ClickHandler handler, String label, String[] args) {
        Player player = handler.getPlayer();
        String regionName = args[0];

        player.closeInventory();
        player.sendMessage(ChatUtil.parse("&aPlease introduce the metadata you want to remove for: " + regionName));
        toRemoveMap.put(player.getUniqueId(), regionName);
        scheduleElimination(player.getUniqueId(), 1000 * 30);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String regionName = toRemoveMap.get(player.getUniqueId());
        if (regionName == null) return;
        RegionsManager manager = RegionsManager.getManager(RegionsManager.class);
        if (manager == null) return;
        String input = event.getMessage();

        event.setCancelled(true);
        manager.getRegion(regionName).ifPresentOrElse(region -> {
            region.getDefaultConfig().removeMetaData(input);
            region.getDefaultConfig().saveMetadata();
            player.sendMessage(ChatUtil.color("&aRemoved metadata " + input));
            toRemoveMap.remove(player.getUniqueId());
        }, () -> {
            player.sendMessage(ChatUtil.color("&cNo region found with name " + regionName));
        });
    }

    private static void scheduleElimination(UUID uuid, long milliseconds) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (toRemoveMap.containsKey(uuid))
                    BukkitUtil.getPlayer(uuid).ifPresent(player -> {
                        player.sendMessage(ChatUtil.color("&cAction for remove metadata has been cancelled due too much time taken!"));
                    });
                toRemoveMap.remove(uuid);
            }
        }, milliseconds);
    }

}

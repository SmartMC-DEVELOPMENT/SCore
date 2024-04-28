package us.smartmc.npcsmodule.listener;

import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.event.PlayerLanguageChangedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.npcsmodule.event.NPCUseEntityEvent;
import us.smartmc.npcsmodule.manager.NPCCommandManager;
import us.smartmc.npcsmodule.manager.NPCManager;
import us.smartmc.smartaddons.plugin.AddonListener;

public class NPCListeners extends AddonListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerDataLoadedEvent event) {
        if (!isEnabled()) return;
        showAllNPCsToPlayer(event.getPlayer());
    }

    @EventHandler
    public void interact(NPCUseEntityEvent event) {
        if (!isEnabled()) return;
        NPCCommandManager.performCommand(event);
    }

    @EventHandler
    public void onLangChange(PlayerLanguageChangedEvent event) {
        NPCManager.forEach(npcManager -> {
            Bukkit.getScheduler().runTaskLater(SpigotPluginsAPI.getPlugin(), () -> {
                npcManager.values().forEach(npc -> {
                    npc.hideTagByPlayerScoreboard(event.getCorePlayer().getBukkitPlayer());
                });
            }, 0);
        });
    }

    private void showAllNPCsToPlayer(Player player) {
        NPCManager.forEach(npcManager -> {
            npcManager.values().forEach(npc -> {
                npc.showTo(player);
            });
        });
    }

}

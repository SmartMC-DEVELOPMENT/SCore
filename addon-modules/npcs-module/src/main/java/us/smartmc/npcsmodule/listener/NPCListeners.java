package us.smartmc.npcsmodule.listener;

import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.event.PlayerLanguageChangedEvent;
import me.imsergioh.pluginsapi.instance.player.CorePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import us.smartmc.core.SmartCore;
import us.smartmc.npcsmodule.event.NPCUseEntityEvent;
import us.smartmc.npcsmodule.manager.NPCCommandManager;
import us.smartmc.npcsmodule.manager.NPCManager;
import us.smartmc.smartaddons.plugin.AddonListener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NPCListeners extends AddonListener implements Listener {

    private static final Set<UUID> recentJoined = new HashSet<>();

    @EventHandler
    public void removeViewer(PlayerQuitEvent event) {
        removeAllViewer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void registerRecentJoined(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        recentJoined.add(uuid);
        Bukkit.getScheduler().runTaskLater(SmartCore.getPlugin(), () -> {
            recentJoined.remove(uuid);
        }, 20);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerDataLoadedEvent event) {
        if (!isEnabled()) return;
        Bukkit.getScheduler().runTask(SmartCore.getPlugin(), () -> {
            showAllNPCsToPlayer(event.getPlayer());
        });
    }

    @EventHandler
    public void interact(NPCUseEntityEvent event) {
        if (!isEnabled()) return;
        NPCCommandManager.performCommand(event);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (recentJoined.contains(event.getPlayer().getUniqueId())) return;
        Location to = event.getTo();
        Location from = event.getFrom();
        if (to == null) return;
        if (to.getWorld() == null || from.getWorld() == null) return;
        // Return if is the same world (Only send npcs if changes world BRO)
        if (to.getWorld().getName().equals(from.getWorld().getName())) return;

        Bukkit.getScheduler().runTask(SmartCore.getPlugin(), () -> {
            showAllNPCsToPlayer(event.getPlayer());
        });
    }

    @EventHandler
    public void onLangChange(PlayerLanguageChangedEvent event) {
        NPCManager.forEach(npcManager -> {
            Bukkit.getScheduler().runTask(SpigotPluginsAPI.getPlugin(), () -> {
                npcManager.values().forEach(npc -> {
                    npc.hideTagByPlayerScoreboard(event.getCorePlayer().getBukkitPlayer());
                });
            });
        });
    }

    private void removeAllViewer(Player player) {
        NPCManager.forEach(npcManager -> {
            npcManager.values().forEach(npc -> {
                npc.removeViewer(player);
            });
        });
    }

    private void showAllNPCsToPlayer(Player player) {
        NPCManager.forEach(npcManager -> {
            npcManager.values().forEach(npc -> {
                npc.checkViewer(player);
                npc.showTo(player);
            });
        });
    }
}

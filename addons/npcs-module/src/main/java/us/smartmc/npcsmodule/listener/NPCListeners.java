package us.smartmc.npcsmodule.listener;

import us.smartmc.core.pluginsapi.spigot.event.PlayerDataLoadedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import us.smartmc.npcsmodule.event.NPCUseEntityEvent;
import us.smartmc.npcsmodule.manager.NPCCommandManager;
import us.smartmc.npcsmodule.manager.NPCManager;
import us.smartmc.smartaddons.plugin.AddonListener;

public class NPCListeners extends AddonListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerDataLoadedEvent event) {
        if (!isEnabled()) return;

        NPCManager.forEach(npcManager -> {
            npcManager.values().forEach(npc -> {
                npc.showTo(event.getPlayer());
            });
        });
    }

    @EventHandler
    public void interact(NPCUseEntityEvent event) {
        if (!isEnabled()) return;
        NPCCommandManager.performCommand(event);
    }

}

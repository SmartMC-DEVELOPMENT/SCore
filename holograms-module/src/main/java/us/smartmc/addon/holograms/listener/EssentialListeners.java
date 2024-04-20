package us.smartmc.addon.holograms.listener;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import us.smartmc.addon.holograms.HologramsAddon;
import us.smartmc.addon.holograms.instance.hologram.HologramHolder;
import us.smartmc.smartaddons.plugin.AddonListener;

public class EssentialListeners extends AddonListener implements Listener {

    private static final HologramsAddon hologramsAddon = HologramsAddon.getPlugin();

    @EventHandler
    public void onJoin(PlayerDataLoadedEvent event) {
        if (!isEnabled()) return;
        Player player = event.getPlayer();
        hologramsAddon.getHologramAdapter().spawnHologramHolder(player, HologramHolder.getOrCreate("main"));
    }
}

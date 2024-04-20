package us.smartmc.addon.holograms.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import us.smartmc.addon.holograms.HologramsAddon;
import us.smartmc.addon.holograms.instance.hologram.HologramHolder;
import us.smartmc.smartaddons.plugin.AddonListener;

public class JoinListener extends AddonListener implements Listener {

    private static final HologramsAddon hologramsAddon = HologramsAddon.getPlugin();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!isEnabled()) return;
        Player player = event.getPlayer();
        hologramsAddon.getHologramAdapter().spawnHologramHolder(player, HologramHolder.getOrCreate("main"));
    }

}

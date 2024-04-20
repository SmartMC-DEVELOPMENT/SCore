package us.smartmc.addon.holograms.listener;

import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.event.PlayerLanguageChangedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
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

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        HologramHolder.forEachHolder(hologramHolder -> {
            hologramHolder.forEachHologram(hologram -> {
                hologram.removeView(event.getPlayer());
            });
        });
    }

    @EventHandler
    public void onLangChange(PlayerLanguageChangedEvent event) {
        updateHolograms(event.getCorePlayer().getBukkitPlayer());
    }

    private void updateHolograms(Player player) {
        HologramHolder.forEachHolder(holder -> {
            holder.forEachHologram(hologram -> {
                if (!hologram.isView(player)) return;
                hologram.removeView(player);
                hologramsAddon.getHologramAdapter().spawnHologramHolder(player, holder);
            });
        });
    }

}

package us.smartmc.addon.holograms.util;

import org.bukkit.entity.Player;
import us.smartmc.addon.holograms.instance.hologram.Hologram;
import us.smartmc.addon.holograms.instance.hologram.HologramArmorStand;
import us.smartmc.addon.holograms.instance.hologram.HologramHolder;

import java.util.Collection;

public interface IHologramAdapter {

    void spawnHologram(Player player, Hologram hologram);
    void updateHologramMetaData(Player player, HologramArmorStand hologramArmorStand);
    void spawnHologramHolder(Player player, HologramHolder holder);

    default Collection<Hologram> getHolograms(String holderName) {
        return getHolder(holderName).getHolograms().values();
    }

    default HologramHolder getHolder(String holderName) {
        return HologramHolder.getOrCreate(holderName);
    }
}

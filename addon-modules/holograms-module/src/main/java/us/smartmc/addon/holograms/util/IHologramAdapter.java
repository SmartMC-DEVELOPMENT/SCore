package us.smartmc.addon.holograms.util;

import org.bukkit.entity.Player;
import us.smartmc.addon.holograms.instance.hologram.*;

import java.util.Collection;

public interface IHologramAdapter {

    void spawnHologram(Player player, IHologram hologram);
    void destroyHologram(Player player, IHologram hologram);

    void updateHologramMetaData(Player player, HologramArmorStand hologramArmorStand);
    void spawnHologramHolder(Player player, HologramHolder holder);

    default Collection<IHologram> getHolograms(String holderName) {
        return getHolder(holderName).getHolograms().values();
    }

    default HologramHolder getHolder(String holderName) {
        return HologramHolder.getOrCreate(holderName);
    }
}

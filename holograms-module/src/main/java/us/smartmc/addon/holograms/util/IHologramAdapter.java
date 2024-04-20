package us.smartmc.addon.holograms.util;

import com.velocitypowered.api.proxy.Player;
import us.smartmc.addon.holograms.instance.hologram.Hologram;
import us.smartmc.addon.holograms.instance.hologram.HologramHolder;

import java.util.Collection;

public interface IHologramAdapter {

    void showHologram(Player player, Hologram hologram);
    void showHologramHolder(Player player, HologramHolder holder);

    default Collection<Hologram> getHolograms(String holderName) {
        return getHolder(holderName).getHolograms().values();
    }

    default HologramHolder getHolder(String holderName) {
        return HologramHolder.getOrCreate(holderName);
    }
}

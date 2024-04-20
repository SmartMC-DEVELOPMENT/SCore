package us.smartmc.addon.holograms.instance.hologram;

import lombok.Getter;
import us.smartmc.addon.holograms.instance.config.HologramHolderConfig;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Getter
public class HologramHolder {

    private static final Map<String, HologramHolder> holders = new HashMap<>();

    private final String name;
    private final HologramHolderConfig config;

    private final Map<String, Hologram> holograms = new HashMap<>();

    protected HologramHolder(String name) {
        this.name = name;
        this.config = new HologramHolderConfig(name);
    }

    public Hologram get(String name) {
        return holograms.get(name);
    }

    public void loadHologram(String name, HologramHolderConfig config) {
        Hologram hologram = new Hologram(name, config);
        holograms.put(name, hologram);
    }

    public void forEach(Consumer<Hologram> consumer) {
        holograms.values().forEach(consumer);
    }

    public static HologramHolder getOrCreate(String name) {
        if (holders.containsKey(name)) return holders.get(name);
        HologramHolder holder = new HologramHolder(name);
        holders.put(name, holder);
        return holder;
    }

}

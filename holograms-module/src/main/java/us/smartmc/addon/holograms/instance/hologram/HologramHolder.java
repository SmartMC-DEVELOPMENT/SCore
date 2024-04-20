package us.smartmc.addon.holograms.instance.hologram;

import lombok.Getter;
import us.smartmc.addon.holograms.instance.config.HologramHolderConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Getter
public class HologramHolder {

    private static final Map<String, HologramHolder> holders = new ConcurrentHashMap<>();

    private final String holderName;
    private final HologramHolderConfig config;

    private final Map<String, Hologram> holograms = new HashMap<>();

    protected HologramHolder(String name) {
        this.holderName = name;
        this.config = new HologramHolderConfig(this);
    }

    public Hologram getHologram(String name) {
        return holograms.get(name);
    }

    public void loadHologram(String hologramName, HologramHolderConfig config) {
        Hologram hologram = new Hologram(hologramName, config);
        holograms.put(hologramName, hologram);
    }

    public void forEachHologram(Consumer<Hologram> consumer) {
        holograms.values().forEach(consumer);
    }

    public static void forEachHolder(Consumer<HologramHolder> consumer) {
        holders.values().forEach(consumer);
    }

    public static HologramHolder getHologramHolder(String name) {
        return holders.get(name);
    }

    public static HologramHolder getOrCreate(String name) {
        if (holders.containsKey(name)) return holders.get(name);
        HologramHolder holder = new HologramHolder(name);
        holders.put(name, holder);
        return holder;
    }

}

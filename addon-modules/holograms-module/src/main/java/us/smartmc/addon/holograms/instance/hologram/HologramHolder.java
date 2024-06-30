package us.smartmc.addon.holograms.instance.hologram;

import lombok.Getter;
import org.bukkit.Location;
import us.smartmc.addon.holograms.instance.config.HologramHolderConfig;
import us.smartmc.addon.holograms.util.LocationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Getter
public class HologramHolder {

    private static final Map<String, HologramHolder> holders = new ConcurrentHashMap<>();

    private final String holderName;

    @Getter
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

    public void updateHologramConfig(String name) {
        Hologram hologram = getHologram(name);
        config.set(HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.START_LOCATION_KEY,
                LocationUtils.locationToString(hologram.getLinesArmorStands().get(0).getStand().getLocation()));

        List<String> lines = new ArrayList<>();
        for (HologramArmorStand hdStand : hologram.getLinesArmorStands()) {
            lines.add(hdStand.getStand().getCustomName());
        }

        config.set(HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.LINES_KEY, lines);

        config.save();
    }

    public void registerHologram(String name, Location location, String text) {
        config.set(HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.START_LOCATION_KEY, LocationUtils.locationToString(location));
        config.set(HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.LINES_KEY, List.of(text.split("\n")));
        config.save();
        loadHologram(name, config);
    }

    public void deleteHologram(String name) {
        config.set(HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name, null);
        config.save();
        getHologram(name).removeAllStands();
        holograms.remove(name);
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

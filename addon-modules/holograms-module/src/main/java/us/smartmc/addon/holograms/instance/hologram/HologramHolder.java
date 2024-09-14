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

    private final Map<String, IHologram> holograms = new HashMap<>();

    protected HologramHolder(String name) {
        this.holderName = name;
        this.config = new HologramHolderConfig(this);
    }

    public IHologram getHologram(String name) {
        return holograms.get(name);
    }

    public void registerHologram(IHologram hologram) {
        holograms.put(hologram.getName(), hologram);
    }

    public void updateHologramConfig(String name) {
        IHologram hologram = getHologram(name);
        config.set(HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.START_LOCATION_KEY,
                LocationUtils.locationToString(hologram.getLinesArmorStands().get(0).getStand().getLocation()));

        List<String> lines = new ArrayList<>();
        for (HologramArmorStand hdStand : hologram.getLinesArmorStands()) {
            lines.add(hdStand.getStand().getCustomName());
        }

        config.set(HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.LINES_KEY, lines);

        config.save();
    }

    public void registerConfigurableHologram(String name, Location location, List<String> lines) {
        config.set(HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.START_LOCATION_KEY, LocationUtils.locationToString(location));
        config.set(HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.LINES_KEY, lines);
        config.save();
        registerHologram(new ConfigurableHologram(name, config));
    }

    public void registerHologram(IHologram hologram, String text) {
        this.registerHologram(hologram, List.of(text.split("\n")));
    }

    public void registerHologram(IHologram hologram, List<String> lines) {
        if (ConfigurableHologram.class.isAssignableFrom(hologram.getClass())) {
            registerConfigurableHologram(hologram.getName(), hologram.getLocation(), lines);
        } else {
            registerHologram(hologram);
        }
    }

    public void deleteHologram(String name) {
        config.set(HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name, null);
        config.save();
        getHologram(name).removeAllStands();
        holograms.remove(name);
    }

    public void forEachHologram(Consumer<IHologram> consumer) {
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

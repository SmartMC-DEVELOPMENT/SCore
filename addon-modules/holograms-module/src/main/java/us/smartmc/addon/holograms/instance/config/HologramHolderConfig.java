package us.smartmc.addon.holograms.instance.config;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import us.smartmc.addon.holograms.HologramsAddon;
import us.smartmc.addon.holograms.instance.hologram.HologramHolder;
import us.smartmc.addon.holograms.util.LocationUtils;

import java.io.File;

@Getter
public class HologramHolderConfig extends SpigotYmlConfig {

    public static final String HOLOGRAMS_MAIN_KEY = "holograms";
    public static final String START_LOCATION_KEY = "location";
    public static final String LINES_KEY = "lines";

    private final HologramHolder holder;

    public HologramHolderConfig(HologramHolder holder) {
        super(new File(HologramsAddon.getPlugin().getDataFolder() + "/holders", holder.getHolderName() + ".yml"));
        this.holder = holder;
        register(MainConfig.UPDATE_RATE_TICKS_KEY, 20);
        loadHolograms();
    }

    public void setLocation(String name, Location location) {
        String locationPath = HologramHolderConfig.HOLOGRAMS_MAIN_KEY + "." + name + "." + HologramHolderConfig.START_LOCATION_KEY;
        set(locationPath, LocationUtils.locationToString(location));
        save();
    }

    private void loadHolograms() {
        if (getConfig() == null) return;
        ConfigurationSection section = getConfig().getConfigurationSection(HOLOGRAMS_MAIN_KEY);
        if (section == null) return;

        for (String name : section.getKeys(false)) {
            holder.loadConfigHologram(name, this);
        }
    }

}

package us.smartmc.addon.holograms.instance.config;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import us.smartmc.addon.holograms.HologramsAddon;
import us.smartmc.addon.holograms.instance.hologram.HologramHolder;

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
        register(START_LOCATION_KEY, 20);
        loadHolograms();
    }

    private void loadHolograms() {
        for (String name : getConfig().getConfigurationSection(HOLOGRAMS_MAIN_KEY).getKeys(false)) {
            holder.loadHologram(name, this);
        }
    }

}

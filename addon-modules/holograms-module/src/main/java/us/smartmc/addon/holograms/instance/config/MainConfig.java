package us.smartmc.addon.holograms.instance.config;

import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import us.smartmc.addon.holograms.HologramsAddon;

import java.io.File;

public class MainConfig extends SpigotYmlConfig {

    public static final String UPDATE_RATE_TICKS_KEY = "updateRateSeconds";

    public MainConfig() {
        super(new File(HologramsAddon.getPlugin().getDataFolder(), "config.yml"));
        register(UPDATE_RATE_TICKS_KEY, 20);
        save();
    }

    public int getUpdateRateTicks() {
        return getInt(UPDATE_RATE_TICKS_KEY);
    }

}

package us.smartmc.lobbymodule.config;

import us.smartmc.core.pluginsapi.instance.FilePluginConfig;
import us.smartmc.smartaddons.plugin.AddonPlugin;

import java.io.File;

public class LobbyConfig extends FilePluginConfig {

    public LobbyConfig(AddonPlugin plugin) {
        super(new File(plugin.getDataFolder(), "lobby.json"));
        load();
        registerDefaults();
        save();
    }

    private void registerDefaults() {
        registerDefault("chat_mode_enabled", true);
        registerDefault("always_sunny_weather", true);
        registerDefault("teleport_at_max_y_loc", false);
        registerDefault("max_y_location", 44);
        registerDefault("set_custom_slot_at_join", true);
        registerDefault("custom_slot_at_join", 4);
        registerDefault("always_day", true);
        registerDefault("always_weather_clear", true);
        registerDefault("cancel_click_event", true);
    }

    public int getCustomSlotArtJoin() {
        return getInteger("custom_slot_at_join");
    }

    public boolean isCustomSlotAtJoinEnabled() {
        return getBoolean("set_custom_slot_at_join");
    }

    public boolean isTeleportAtMaxY() {
        return getBoolean("teleport_at_max_y_loc");
    }

    public boolean isChatModeEnabled() {
        return getBoolean("chat_mode_enabled");
    }

}

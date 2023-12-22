package us.smartmc.snowgames.config;

import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.object.PluginConfig;

public class DefaultConfig extends PluginConfig {

    private static final FFAPlugin plugin = FFAPlugin.getPlugin();

    public DefaultConfig() {
        super(plugin.getDataFolder() + "/config.yml");

        registerCooldown("propeller", 5);
        registerCooldown("speed", 15);
        registerCooldown("blockRestoration", 20);

        save();
    }

    private void registerCooldown(String path, int value) {
        registerDefault(getCooldownPath(path), value);
    }

    public static int getCooldown(String path) {
        return plugin.getDefaultConfig().getInt(getCooldownPath(path));
    }

    private static String getCooldownPath(String path) {
        return "cooldown." + path;
    }

}

package us.smartmc.snowgames.config;

import me.imsergioh.pluginsapi.connection.RedisConnection;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.object.PluginConfig;

public class DefaultConfig extends PluginConfig {

    private static final FFAPlugin plugin = FFAPlugin.getFFAPlugin();

    public DefaultConfig() {
        super(plugin.getDataFolder() + "/config.yml");

        registerCooldown("propeller", 5);
        registerCooldown("speed", 15);
        registerCooldown("blockRestoration", 20);

        registerDefault("enabled-join-message", false);
        registerDefault("join-message", "&8[&a+&8] &7{0}");

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


    public static String getJoinMessage() {
        return FFAPlugin.getFFAPlugin().getDefaultConfig().getString("join-message");
    }

    public static boolean isJoinMessageEnabled() {
        return isBoolean("enabled-join-message");
    }

    public static boolean isBoolean(String path) {
        return FFAPlugin.getFFAPlugin().getDefaultConfig().getBoolean(path);
    }

}

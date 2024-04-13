package us.smartmc.snowgames.config;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import org.bukkit.Material;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.object.PluginConfig;

public class LanguageConfig extends PluginConfig {

    private static final FFAPlugin plugin = FFAPlugin.getFFAPlugin();

    public LanguageConfig() {
        super("/home/network/config/snowgames-ffa" + "/language.yml");

        registerMultiLanguageItem("weapon", ItemBuilder.of(Material.STICK).name("Weapon"));
        registerMultiLanguageItem("blocks", ItemBuilder.of(Material.SANDSTONE).amount(32).name("Blocks"));
        registerMultiLanguageItem("propeller", ItemBuilder.of(Material.LEGACY_GOLD_PLATE).name("Propeller"));
        registerMultiLanguageItem("speed", ItemBuilder.of(Material.FEATHER).name("Speed"));
        registerMultiLanguageItem("regeneration", ItemBuilder.of(Material.GOLDEN_APPLE).name("Regeneration"));

        registerMultiLanguageItem("propeller_reloading", ItemBuilder.of(Material.STONE_PRESSURE_PLATE).name("Propeller &7&o(reloading...)"));
        registerMultiLanguageItem("speed_reloading", ItemBuilder.of(Material.SUGAR).name("Speed &7&o(reloading...)"));

        registerMultiLanguageItem("lobby.tops", ItemBuilder.of(Material.DIAMOND).name("&6Tops"));
        registerMultiLanguageItem("lobby.settings", ItemBuilder.of(Material.CLOCK).name("&bSettings"));
        registerMultiLanguageItem("lobby.missions", ItemBuilder.of(Material.CLOCK).name("&aMissions"));

        registerMultiLanguageItem("general.in_development", ItemBuilder.of(Material.BARRIER).name("&cIn development!"));

        save();
    }

    public static String getMessage(String pathName) {
        return plugin.getLanguageConfig().getString(pathName);
    }

    public static String getName(String path) {
        return path;
    }

}

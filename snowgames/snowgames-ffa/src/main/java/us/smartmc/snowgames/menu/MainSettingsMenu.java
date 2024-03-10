package us.smartmc.snowgames.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.Material;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.config.LanguageConfig;
import us.smartmc.snowgames.messages.PluginMessages;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainSettingsMenu extends FFAMenu {

    private static final FFAPlugin plugin = FFAPlugin.getFFAPlugin();
    private static final LanguageConfig config = plugin.getLanguageConfig();
    private static final Map<Language, MainSettingsMenu> menus = new HashMap<>();
    private static final String CONFIG_PATH = "menu.main_settings";

    public static MainSettingsMenu get(Language language) {
        if (menus.containsKey(language)) return menus.get(language);
        return new MainSettingsMenu(language);
    }

    private final Language language;

    private MainSettingsMenu(Language language) {
        super(null, 9 * 3, PluginMessages.getMenuTitle(language, "settings"));
        this.language = language;
        menus.put(language, this);
        initConfigDefaults();
    }

    @Override
    public void load() {
        set(12, config.getItemConfig(language, CONFIG_PATH + ".blocks").get(), "settings blocks");
        set(14, config.getItemConfig(language, CONFIG_PATH + ".projectiles").get(), "settings projectiles");
    }

    public void initConfigDefaults() {
        config.registerItemConfig(language, CONFIG_PATH + ".blocks", ItemBuilder
                .of(Material.SANDSTONE)
                .name("&aPersonalize your blocks")
                .lore(Arrays.asList("Change your blocks at ingame inventory")));

        config.registerItemConfig(language, CONFIG_PATH + ".projectiles", ItemBuilder
                .of(Material.SNOW_BALL)
                .name("&aPersonalize your projectile")
                .lore(Arrays.asList("Change your projectile throw in the game.", "Normally you throw snowballs but you can personalize this to another time of projectiles")));
    }

}

package us.smartmc.snowgames.menu;

import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.smartmc.snowgames.config.LanguageConfig;

import java.util.Arrays;

public class SettingsMenu extends FFAMenu {


    public SettingsMenu(Player player) {
        super(player, 9*3, LanguageConfig.getMenuTitle("settings"));
    }

    @Override
    public void load() {
        set(13, ItemBuilder.of(Material.BARRIER)
                .name("&c&lERROR")
                .lore(Arrays.asList("&7<lang.snowgames/ffa/main.not_available>"))
                .get(player));
    }
}

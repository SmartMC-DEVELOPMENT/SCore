package us.smartmc.snowgames.inventory;

import org.bukkit.entity.Player;
import us.smartmc.core.pluginsapi.spigot.menu.CoreMenu;
import us.smartmc.snowgames.config.LanguageConfig;

public class SettingsMenu extends FFAMenu {
    public SettingsMenu(Player player) {
        super(player, 9*5, LanguageConfig.getMenuTitle("settings"));
    }

    @Override
    public void load() {

    }
}

package us.smartmc.snowgames.inventory;

import org.bukkit.entity.Player;
import us.smartmc.core.pluginsapi.spigot.menu.CoreMenu;
import us.smartmc.snowgames.config.LanguageConfig;

public class MissionsMenu extends FFAMenu {
    protected MissionsMenu(Player player) {
        super(player, 9*6, LanguageConfig.getMenuTitle("missions"));
    }

    @Override
    public void load() {

    }
}

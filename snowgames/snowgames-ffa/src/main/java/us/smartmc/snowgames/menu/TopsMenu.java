package us.smartmc.snowgames.menu;

import org.bukkit.entity.Player;
import us.smartmc.snowgames.config.LanguageConfig;

public class TopsMenu extends FFAMenu {
    public TopsMenu(Player player) {
        super(player, 27, LanguageConfig.getMenuTitle("tops"));
    }

    @Override
    public void load() {

    }
}

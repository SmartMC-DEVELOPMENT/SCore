package us.smartmc.snowgames.actions;

import org.bukkit.entity.Player;
import us.smartmc.core.pluginsapi.spigot.instance.ItemActionExecutor;
import us.smartmc.core.pluginsapi.spigot.item.ClickHandler;
import us.smartmc.snowgames.inventory.SettingsMenu;
import us.smartmc.snowgames.inventory.TopsMenu;
import us.smartmc.snowgames.util.GameItemUtils;

public class HotbarActions implements ItemActionExecutor {


    @Override
    public void execute(ClickHandler handler, String s, String[] args) {
        Player player = handler.getPlayer();
        String arg = args[0].toLowerCase();

        if (arg.equals("tops")) {
            GameItemUtils.openMenu(player, TopsMenu.class);
        }

        if (arg.equals("settings")) {
            GameItemUtils.openMenu(player, SettingsMenu.class);
        }
    }
}

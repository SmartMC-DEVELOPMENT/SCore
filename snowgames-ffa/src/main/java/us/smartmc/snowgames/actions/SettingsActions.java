package us.smartmc.snowgames.actions;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import org.bukkit.entity.Player;
import us.smartmc.snowgames.menu.SettingMenu;
import us.smartmc.snowgames.util.GameItemUtils;

public class SettingsActions implements ItemActionExecutor {


    @Override
    public void execute(ClickHandler handler, String s, String[] args) {
        Player player = handler.getPlayer();
        String arg = args[0].toLowerCase();

        new SettingMenu(arg, player).open(player);
    }
}

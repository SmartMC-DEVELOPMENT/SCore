package us.smartmc.snowgames.actions;

import me.imsergioh.pluginsapi.instance.ItemActionExecutor;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.item.ClickHandler;
import org.bukkit.entity.Player;
import us.smartmc.snowgames.menu.MainSettingsMenu;
import us.smartmc.snowgames.menu.TopsMenu;
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
            MainSettingsMenu.get(PlayerLanguages.get(player.getUniqueId())).open(player);
        }
    }
}

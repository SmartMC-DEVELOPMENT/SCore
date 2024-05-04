package us.smartmc.core.luckywars.menu;

import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.language.IMessageCategory;
import org.bukkit.entity.Player;
import us.smartmc.core.luckywars.util.MenuUtil;

public abstract class GameMenu extends CoreMenu {

    protected GameMenu(Player player, int size, IMessageCategory title) {
        super(player, size, getTitle(player, title));
    }

    private static String getTitle(Player player, IMessageCategory title) {
        return MenuUtil.getTitle(player, title);
    }

}

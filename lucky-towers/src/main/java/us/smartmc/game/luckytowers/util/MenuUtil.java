package us.smartmc.game.luckytowers.util;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.IMessageCategory;
import org.bukkit.entity.Player;

public class MenuUtil {

    public static String getTitle(Player player, IMessageCategory category) {
        return category.getMessageOf(PlayerLanguages.get(player.getUniqueId()));
    }

}

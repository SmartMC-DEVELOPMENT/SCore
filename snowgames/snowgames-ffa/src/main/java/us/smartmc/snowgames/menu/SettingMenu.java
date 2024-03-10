package us.smartmc.snowgames.menu;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.instance.menu.CoreMenu;
import me.imsergioh.pluginsapi.language.Language;
import org.bukkit.entity.Player;
import us.smartmc.snowgames.messages.PluginMessages;

import java.text.MessageFormat;

public class SettingMenu extends CoreMenu {

    public SettingMenu(String id, Player player) {
        super(player, 27, getTitle(id, player));
    }

    @Override
    public void load() {

    }

    public static String getTitle(String id, Player player) {
        Language language = PlayerLanguages.get(player.getUniqueId());
        return MessageFormat.format(PluginMessages.getMenuTitle(language, id), PluginMessages.getSettingName(language, id));
    }

}

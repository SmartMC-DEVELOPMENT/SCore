package us.smartmc.core.handler;

import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bukkit.entity.Player;
import us.smartmc.core.instance.data.ListRegistry;

import java.util.UUID;

public class AdminModeHandler extends ListRegistry<UUID> {

    public void toggle(Player player) {
        if (!isActive(player)) {
            register(player.getUniqueId());
            player.sendMessage(ChatUtil.parse(player, "&aEnabled"));
        } else {
            unregister(player.getUniqueId());
            player.sendMessage(ChatUtil.parse(player, "&cDisabled"));
        }
    }

    public boolean isActive(Player player) {
        return contains(player.getUniqueId());
    }

}

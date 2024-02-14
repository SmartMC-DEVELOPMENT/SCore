package us.smartmc.addon.listener;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.smartmc.addon.handler.ChatModeHandler;
import us.smartmc.smartaddons.plugin.AddonListener;

public class ChatModeListener extends AddonListener implements Listener {

    private final ChatModeHandler handler;

    public ChatModeListener(ChatModeHandler handler) {
        this.handler = handler;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!isEnabled()) return;
        String format = ChatUtil.parse(event.getPlayer(), handler.getFormat());
        if (event.getPlayer().hasPermission("smartmc.vip")) {
            event.setMessage(ChatUtil.color(event.getMessage()));
        }
        event.setFormat(format);
    }

}

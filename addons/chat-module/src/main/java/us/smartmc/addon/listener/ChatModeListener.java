package us.smartmc.addon.listener;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.smartmc.addon.handler.ChatModeHandler;
import us.smartmc.smartaddons.plugin.AddonListener;

public class ChatModeListener extends AddonListener implements Listener {

    private final ChatModeHandler handler;

    public ChatModeListener(ChatModeHandler handler) {
        this.handler = handler;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!isEnabled()) return;
        String format = handler.getFormat();
        event.setFormat(ChatUtil.parse(event.getPlayer(), String.format(format, event.getMessage())));
    }

}

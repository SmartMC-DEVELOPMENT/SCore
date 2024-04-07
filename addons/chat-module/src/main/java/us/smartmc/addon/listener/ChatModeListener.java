package us.smartmc.addon.listener;

import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.smartmc.addon.handler.ChatModeHandler;
import us.smartmc.core.SmartCore;
import us.smartmc.smartaddons.plugin.AddonListener;

public class ChatModeListener extends AddonListener implements Listener {

    private final ChatModeHandler handler;

    public ChatModeListener(ChatModeHandler handler) {
        this.handler = handler;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!isEnabled()) return;
        String format = PaperChatUtil.parse(event.getPlayer(), handler.getFormat());
        if (event.getPlayer().hasPermission("smartmc.vip")) {
            String message = PaperChatUtil.color("&f" + event.getMessage());
            if (SmartCore.getPlugin().getAdminModeHandler().isActive(event.getPlayer())) {
                message = PaperChatUtil.parse(event.getPlayer(), message);
            }
            event.setMessage(message);
        } else {
            event.setMessage(PaperChatUtil.color("&7") + event.getMessage());
        }
        event.setFormat(format);
        event.setCancelled(true);
        Bukkit.broadcastMessage(String.format(event.getFormat(), event.getPlayer().getName(), event.getMessage()));
    }

}
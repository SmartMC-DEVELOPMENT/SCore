package us.smartmc.addon.listener;

import me.imsergioh.pluginsapi.util.ChatUtil;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import us.smartmc.addon.handler.ChatModeHandler;
import us.smartmc.core.SmartCore;
import us.smartmc.smartaddons.plugin.AddonListener;

public class ChatModeListener extends AddonListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!isEnabled()) return;
        if (event.isCancelled()) return;
        String message = event.getMessage();
        if (event.getPlayer().hasPermission("smartmc.vip")) {
            message = ChatUtil.color(event.getMessage());
        }

        String format = ChatUtil.parse(event.getPlayer(), "<chat.prefix><name> &8&l»&r ") + message;

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(format);
        }
        Bukkit.getConsoleSender().sendMessage(format);
        event.setCancelled(true);
    }
}
package us.smartmc.moderation.staffmodebungee.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import us.smartmc.moderation.staffmodebungee.StaffModeMainBungee;
import us.smartmc.moderation.staffmodebungee.instance.StaffChatMessageRequest;
import us.smartmc.moderation.staffmodebungee.manager.MessagesManager;
import us.smartmc.moderation.staffmodebungee.manager.StaffChatManager;
import us.smartmc.moderation.staffmodebungee.message.StaffMessages;
import us.smartmc.moderation.staffmodebungee.util.RegistrationUtil;

public class StaffChatEvent implements Listener {

    private static final StaffChatManager chatManager = StaffModeMainBungee.getChatManager();

    @EventHandler(priority = 12)
    public void onChat(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) return;
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String message = event.getMessage();
        if (message.startsWith("/")) return;
        if (!player.hasPermission(RegistrationUtil.STAFF_PERMISSION)) return;
        if (!chatManager.isToggledChat(player)) return;
        event.setCancelled(true);
        if (chatManager.hasChatDisabled(player)) {
            MessagesManager.send(player, StaffMessages.class, "chat.visibility-not-enabled");
            return;
        }
        // Create instance of message + publish
        new StaffChatMessageRequest(player.getName(), message).publish();
    }
}

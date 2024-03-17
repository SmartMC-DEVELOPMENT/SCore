package us.smartmc.moderation.staffmodebungee.manager;

import me.imsergioh.pluginsapi.connection.RedisConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import redis.clients.jedis.Jedis;
import us.smartmc.moderation.staffmodebungee.StaffModeMainBungee;
import us.smartmc.moderation.staffmodebungee.instance.StaffChatMessageRequest;
import us.smartmc.moderation.staffmodebungee.message.StaffMessages;
import us.smartmc.moderation.staffmodebungee.util.RegistrationUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class StaffChatManager {

    private static final String SET_CHAT_DISABLED_NAME = "staffmode.chat-disabled";

    private static final Set<UUID> chattingList = new HashSet<>();

    public boolean isToggledChat(ProxiedPlayer player) {
        return chattingList.contains(player.getUniqueId());
    }

    public boolean toggleChat(ProxiedPlayer player) {
        if (!chattingList.contains(player.getUniqueId())) {
            // ENABLE
            chattingList.add(player.getUniqueId());
            MessagesManager.send(player, StaffMessages.class, "chat.enabled");
            return true;
        }
        // DISABLE
        chattingList.remove(player.getUniqueId());
        MessagesManager.send(player, StaffMessages.class, "chat.disabled");
        return false;
    }

    public void broadcastMessage(StaffChatMessageRequest message) {
        for (ProxiedPlayer player : StaffModeMainBungee.getPlugin().getProxy().getPlayers()) {
            if (!player.hasPermission(RegistrationUtil.STAFF_PERMISSION)) continue;
            if (hasChatDisabled(player)) continue;
            player.sendMessage(message.getFormattedMessage(player));
        }
    }

    public boolean toggleVisibility(ProxiedPlayer player) {
        if (hasChatDisabled(player)) {
            // ENABLE
            getJedis().srem(SET_CHAT_DISABLED_NAME, id(player));
            MessagesManager.send(player, StaffMessages.class, "chat.visibility-enabled");
            return true;
        }
        // DISABLE
        getJedis().sadd(SET_CHAT_DISABLED_NAME, id(player));
        MessagesManager.send(player, StaffMessages.class, "chat.visibility-disabled");
        return false;
    }

    public boolean hasChatDisabled(ProxiedPlayer player) {
        return getJedis().sismember(SET_CHAT_DISABLED_NAME, id(player));
    }

    private String id(ProxiedPlayer player) {
        return player.getUniqueId().toString();
    }

    private Jedis getJedis() {
        return RedisConnection.mainConnection.getResource();
    }

}

package us.smartmc.moderation.staffmodebungee.manager;

import me.imsergioh.pluginsapi.connection.RedisConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import redis.clients.jedis.Jedis;
import us.smartmc.moderation.staffmodebungee.message.StaffMessages;

public class StaffChatManager {

    private static final String SET_DISABLED_NAME = "staffmode.chat-disabled";

    public boolean toggleChat(ProxiedPlayer player) {
        if (!hasChatDisabled(player)) {
            // DISABLE
            getJedis().srem(SET_DISABLED_NAME, id(player));
            MessagesManager.send(player, StaffMessages.class, "chat.disabled");
            return false;
        }
        // ENABLE
        getJedis().sadd(SET_DISABLED_NAME, id(player));
        MessagesManager.send(player, StaffMessages.class, "chat.enabled");
        return true;
    }

    public boolean hasChatDisabled(ProxiedPlayer player) {
        return getJedis().sismember(SET_DISABLED_NAME, id(player));
    }

    private String id(ProxiedPlayer player) {
        return player.getUniqueId().toString();
    }

    private Jedis getJedis() {
        return RedisConnection.mainConnection.getResource();
    }

}

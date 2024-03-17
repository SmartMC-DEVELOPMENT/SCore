package us.smartmc.moderation.staffmodebungee.rediscommand;

import me.imsergioh.pluginsapi.instance.handler.RedisPubSubListener;
import us.smartmc.moderation.staffmodebungee.StaffModeMainBungee;
import us.smartmc.moderation.staffmodebungee.instance.StaffChatMessageRequest;
import us.smartmc.moderation.staffmodebungee.manager.StaffChatManager;

public class MessageCommand extends RedisPubSubListener {

    private static final StaffChatManager chatManager = StaffModeMainBungee.getChatManager();

    public MessageCommand() {
        super(StaffChatMessageRequest.CHANNEL_NAME);
    }

    @Override
    public void onMessage(String message) {
        StaffChatMessageRequest request = StaffChatMessageRequest.fromJson(message);
        chatManager.broadcastMessage(request);
    }
}

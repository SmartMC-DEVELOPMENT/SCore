package us.smartmc.backend.handler;

import us.smartmc.backend.instance.messaging.MessageChannel;

import java.util.*;

public class MessagingChannelsManager {

    private static final Map<String, MessageChannel> channelsRegistry = new HashMap<>();

    public static void register(MessageChannel... channels) {
        for (MessageChannel channel : channels) {
            channelsRegistry.put(channel.getId(), channel);
        }
    }

    public static boolean perform(String channelId, String message) {
        MessageChannel channel = channelsRegistry.get(channelId);
        if (channel == null) return false;
        channel.onMessage(channelId, message);
        return true;
    }

}

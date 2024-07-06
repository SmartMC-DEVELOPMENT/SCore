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

    public static void perform(String channelId, String message) {
        MessageChannel channel = channelsRegistry.get(channelId);
        if (channel == null) return;
        channel.onMessage(channelId, message);
    }

}

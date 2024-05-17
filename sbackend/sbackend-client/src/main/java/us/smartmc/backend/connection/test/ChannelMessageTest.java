package us.smartmc.backend.connection.test;

import us.smartmc.backend.instance.messaging.MessageChannel;

public class ChannelMessageTest extends MessageChannel {

    public ChannelMessageTest() {
        super("test:main");
    }

    @Override
    public void onMessage(String channelId, String message) {
        System.out.println("RECEIVED MESSAGE FROM " + channelId + " " + message);
    }
}

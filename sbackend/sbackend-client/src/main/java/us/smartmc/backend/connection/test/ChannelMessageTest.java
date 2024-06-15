package us.smartmc.backend.connection.test;

import us.smartmc.backend.connection.TestMain;
import us.smartmc.backend.instance.messaging.MessageChannel;

public class ChannelMessageTest extends MessageChannel {

    public ChannelMessageTest() {
        super("test");
    }

    @Override
    public void onMessage(String channelId, String message) {
        TestMain.timingTest.registerEnd();
        long difference = TestMain.timingTest.getDifference();
        System.out.println("RECEIVED MESSAGE FROM " + channelId + " " + message + " " + difference + "ms");
    }
}

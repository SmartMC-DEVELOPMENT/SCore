package us.smartmc.backend.connection;

import us.smartmc.backend.connection.command.TestCommand;
import us.smartmc.backend.connection.listener.CacheCompleteListener;
import us.smartmc.backend.connection.test.ChannelMessageTest;
import us.smartmc.backend.connection.test.TimingTestInstance;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.handler.MessagingChannelsManager;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.protocol.CommandRequest;

public class TestMain {

    private static BackendClient client;

    public static final TimingTestInstance timingTest = new TimingTestInstance();

    public static void main(String[] initArgs)  {
        try {
            ConnectionInputManager.registerListeners(new CacheCompleteListener());
            client = new BackendClient("play.smartmc.us", 7723);
            client.login("default", "SmartMC2024Ñ");
            new Thread(client).start();
            client.broadcastCommand(null, "sendVelocityMsg {\"_id\": \"imsergioh\", \"message\": \"<lang.discordbot/main.LINKED.DISCORD.SUCCESSFULLY>\", \"args\": [\"imsergioh\"]}");
        } catch (Exception e) {
            client.handleException(e);
        }
    }
}

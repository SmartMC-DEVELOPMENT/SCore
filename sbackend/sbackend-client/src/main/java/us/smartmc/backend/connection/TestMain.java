package us.smartmc.backend.connection;

import us.smartmc.backend.connection.listener.CacheCompleteListener;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.handler.MessagingChannelsManager;
import us.smartmc.backend.instance.messaging.MessageChannel;

public class TestMain extends MessageChannel {

    private static BackendClient client;

    private static long start;

    public TestMain(String id) {
        super(id);
    }

    public static void main(String[] initArgs)  {
        try {
            ConnectionInputManager.registerListeners(new CacheCompleteListener());
            client = new BackendClient("66.70.181.34", 7723);
            client.login("default", "SmartMC2024Ñ");
            new Thread(client).start();

            MessagingChannelsManager.register(new TestMain("test:main"));

            client.subscribe("test:main");

            for (int i = 0; i < 99; i++) {
                start = System.currentTimeMillis();
                client.publishMessage("test:main", "Un mensaje totalmente normal sin ningún tipo de duda");
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            client.handleException(e);
        }
    }

    @Override
    public void onMessage(String channelId, String message) {
        long end = System.currentTimeMillis();
        long difference = end - start;
        System.out.println("RECEIVED MESSASGE CHANNEL " + channelId + " " + message + " (" + difference + "ms)");
    }
}

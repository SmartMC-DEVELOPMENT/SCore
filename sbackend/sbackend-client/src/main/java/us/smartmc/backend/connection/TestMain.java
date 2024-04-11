package us.smartmc.backend.connection;

import us.smartmc.backend.connection.command.TestCommand;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.protocol.UTFMessage;

import java.util.UUID;

public class TestMain implements BackendObjectListener {

    private static TestMain main;

    public static void main(String[] initArgs) throws Exception {
        main = new TestMain();
        ConnectionInputManager.registerListeners(main);
        ConnectionInputManager.registerCommands(new TestCommand());

        BackendClient client = new BackendClient("66.70.181.34", 7723);
        client.login("default", "asd");
        new Thread(() -> {
            while (true) {
                UUID id = UUID.randomUUID();
                client.sendMessage("GETPLAYERCACHEKEY " + id + " testvalue");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        client.run();
    }

    @Override
    public void onReceive(ConnectionHandler connection, Object object) {
        System.out.println(System.currentTimeMillis());
        if (object instanceof UTFMessage utfMessage) {
            System.out.println("New UTF message received = " + utfMessage.getMessage());
        }
    }
}

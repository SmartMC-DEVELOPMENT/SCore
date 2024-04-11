package us.smartmc.backend.connection;

import us.smartmc.backend.connection.command.TestCommand;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.BackendUTFListener;


public class TestMain implements BackendUTFListener {

    private static TestMain main;

    public static void main(String[] initArgs) throws Exception {
        main = new TestMain();
        ConnectionInputManager.registerListeners(main);
        ConnectionInputManager.registerCommands(new TestCommand());

        BackendClient client = new BackendClient("66.70.181.34", 7723);
        client.login("default", "asd");
        new Thread(() -> {
            while (true) {
                long start = System.currentTimeMillis();
                client.sendCommand("helloWorld " + start);
                System.out.println("SENT! " + start);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        client.run();
    }

    @Override
    public void onReceive(ConnectionHandler connection, String utf) {
        long end = System.currentTimeMillis();
        System.out.println("RESPONSE! " + end + "(" + utf + ")");
    }
}

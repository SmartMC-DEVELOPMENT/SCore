package us.smartmc.backend.connection;

import us.smartmc.backend.connection.command.TestCommand;
import us.smartmc.backend.connection.listener.CacheCompleteListener;
import us.smartmc.backend.connection.test.TimingTestInstance;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.protocol.CommandRequest;

public class TestMain {

    private static BackendClient client;

    public static final TimingTestInstance timingTest = new TimingTestInstance();

    public static void main(String[] initArgs)  {
        try {
            ConnectionInputManager.registerListeners(new CacheCompleteListener());
            client = new BackendClient("66.70.181.34", 7723);
            client.login("default", "SmartMC2024Ñ");
            new Thread(client).start();
            ConnectionInputManager.registerCommands(new TestCommand());

            client.subscribeContext("test");

            for (int i = 0; i < 99; i++) {
                timingTest.registerStart();
                client.broadcastCommand(null, "test");
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            client.handleException(e);
        }
    }
}

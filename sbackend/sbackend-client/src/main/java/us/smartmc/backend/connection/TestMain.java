package us.smartmc.backend.connection;

import us.smartmc.backend.connection.listener.CacheCompleteListener;
import us.smartmc.backend.connection.test.TimingTestInstance;
import us.smartmc.backend.handler.ConnectionInputManager;

import java.util.UUID;

public class TestMain {

    private static BackendClient client;

    public static final TimingTestInstance timingTest = new TimingTestInstance();

    public static void main(String[] initArgs)  {
        try {
            ConnectionInputManager.registerListeners(new CacheCompleteListener());
            client = new BackendClient("localhost", 7723);
            client.login("default", "SmartMC2024Ñ");
            new Thread(client).start();
            client.registerCache("testCache", UUID.randomUUID());
            client.getCache("testCache", o -> {
                System.out.print("O = " + o.toString() + " " + o.getClass().getName());
            });
        } catch (Exception e) {
            client.handleException(e);
        }
    }
}

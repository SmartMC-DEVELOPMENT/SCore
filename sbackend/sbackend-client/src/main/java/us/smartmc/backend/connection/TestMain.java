package us.smartmc.backend.connection;

import us.smartmc.backend.connection.listener.CacheCompleteListener;
import us.smartmc.backend.handler.ConnectionInputManager;


public class TestMain {

    private static BackendClient client;

    public static void main(String[] initArgs)  {
        try {
            ConnectionInputManager.registerListeners(new CacheCompleteListener());
            client = new BackendClient("localhost", 7723);
            client.login("default", "SmartMC2024Ñ");
            new Thread(client).start();
        } catch (Exception e) {
            client.handleException(e);
            throw new RuntimeException(e);
        }
    }
}

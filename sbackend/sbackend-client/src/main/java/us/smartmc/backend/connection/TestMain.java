package us.smartmc.backend.connection;

import us.smartmc.backend.connection.listener.CacheCompleteListener;
import us.smartmc.backend.handler.ConnectionInputManager;

import java.io.PrintWriter;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;


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

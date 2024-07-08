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
            StringBuilder s = new StringBuilder("iadjfapdf");

            while (true) {
                client.setCache("keyRandom", 13);

                long start = System.currentTimeMillis();
                client.getCache("keyRandom", wrapper -> {
                    wrapper.getOptionalOfClass(Integer.class).ifPresent(string -> System.out.println("Entero " + string));
                    long end = System.currentTimeMillis();
                    long diff = end - start;
                    System.out.println("START " + start + " END " + end);
                    System.out.println("DURATION = " + diff + " ms");
                });

                Thread.sleep(1000);
            }
        } catch (Exception e) {
            client.handleException(e);
            throw new RuntimeException(e);
        }
    }
}

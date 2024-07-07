package us.smartmc.backend.connection;

import us.smartmc.backend.connection.listener.CacheCompleteListener;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.filetransfer.FileTransferType;

import java.io.File;


public class TestMain {

    private static BackendClient client;

    public static void main(String[] initArgs)  {
        try {
            ConnectionInputManager.registerListeners(new CacheCompleteListener());
            client = new BackendClient("localhost", 7723);
            client.login("default", "SmartMC2024Ñ");
            new Thread(client).start();
            new Thread(() -> {
                System.out.println("Preparando transferencia...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                client.sendFile(new File("C:\\Users\\sergi\\Documents\\EVENTOS.txt"), FileTransferType.CACHE, "C:\\Users\\sergi\\Desktop\\test\\testFile.txt");
            }).start();
        } catch (Exception e) {
            client.handleException(e);
            throw new RuntimeException(e);
        }
    }
}

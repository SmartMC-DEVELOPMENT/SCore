package us.smartmc.backend.connection;

import us.smartmc.backend.connection.listener.CacheCompleteListener;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.cache.CacheFile;
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



            while (true) {
                long start = System.currentTimeMillis();
                client.downloadFile("C:\\Users\\sergi\\Desktop\\test\\backup.sql",
                        FileTransferType.PERMANENT,
                        "C:\\Users\\sergi\\Desktop\\test\\sqlbackuplol.sql", fileWrapper -> {
                            long end = System.currentTimeMillis();
                            System.out.println("ASDOUFOAIJFSOIFJDSO");
                            System.out.println("ID=" + fileWrapper.getId());
                            System.out.println("HEMOS RECIBIDO ARCHIVO! " + (end - start) + "ms");
                        });
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            client.handleException(e);
            throw new RuntimeException(e);
        }
    }
}

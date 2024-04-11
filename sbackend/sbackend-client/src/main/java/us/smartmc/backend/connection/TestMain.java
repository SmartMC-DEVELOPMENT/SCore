package us.smartmc.backend.connection;

import us.smartmc.backend.connection.command.TestCommand;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.BackendUTFListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class TestMain implements BackendUTFListener {

    private static TestMain main;

    public static void main(String[] initArgs) throws Exception {
        main = new TestMain();
        ConnectionInputManager.registerListeners(main);
        ConnectionInputManager.registerCommands(new TestCommand());

        BackendClient client = new BackendClient("66.70.181.34", 7723);
        client.login("default", "asd");

        new Thread(client).start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (reader.readLine() != null) {
            long start = System.currentTimeMillis();
            client.sendCommand("helloWorld " + start);
            System.out.println("SENT! " + start);
        }
    }

    @Override
    public void onReceive(ConnectionHandler connection, String utf) {
        long end = System.currentTimeMillis();
        System.out.println("RESPONSE! " + end + "(" + utf + ")");
    }
}

package us.smartmc.backend.connection;

import us.smartmc.backend.connection.command.TestCommand;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.protocol.UTFMessage;

import java.io.IOException;

public class TestMain implements BackendObjectListener {

    private static TestMain main;

    public static void main(String[] args) throws IOException {
        main = new TestMain();
        ConnectionInputManager.registerListeners(main);
        ConnectionInputManager.registerCommands(new TestCommand());

        BackendClient client = new BackendClient("smart.dedi1", 7723);
        client.login("default", "EstoEsUnaContraseñaSeguraParaLosLobby");
        client.run();
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onReceive(ConnectionHandler connection, Object object) {
        if (object instanceof UTFMessage utfMessage) {
            System.out.println("New UTF message received = " + utfMessage.getMessage());
        }
    }
}

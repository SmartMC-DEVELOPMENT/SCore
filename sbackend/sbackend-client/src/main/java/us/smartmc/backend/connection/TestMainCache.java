package us.smartmc.backend.connection;

import us.smartmc.backend.connection.command.SetPlayerDataCmd;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.BackendUTFListener;

public class TestMainCache implements BackendUTFListener {

    public static void main(String[] initArgs) throws Exception {
        TestMainCache main = new TestMainCache();
        ConnectionInputManager.registerListeners(main);
        ConnectionInputManager.registerCommands(new SetPlayerDataCmd());

        BackendClient client = new BackendClient("66.70.181.34", 7723);
        client.login("default", "contraseñaSEGURAASDññññññ");

        client.run();
    }

    @Override
    public void onReceive(ConnectionHandler connection, String utf) {
        long end = System.currentTimeMillis();
        System.out.println("RESPONSE! " + end + "(" + utf + ")");
    }
}

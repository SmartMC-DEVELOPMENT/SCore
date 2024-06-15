package us.smartmc.game.backend;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.protocol.LoginCompleted;
import us.smartmc.game.SkyBlockPlugin;

public class SendInfoListener extends BackendObjectListener<LoginCompleted> {

    @Override
    public void onReceive(ConnectionHandler connection, LoginCompleted object) {
        SkyBlockPlugin.registerServerToBackend();
    }
}

package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.BackendUTFListener;
import us.smartmc.backend.protocol.CommandRequest;

public class CommandHandlerListener implements BackendUTFListener {

    @Override
    public void onReceive(ConnectionHandler connection, String utf) {
        if (utf.startsWith("CMD@")) {
            String labelCommand = utf.replaceFirst("CMD@", "");
            ConnectionInputManager.performCommand(connection, new CommandRequest(labelCommand));
        }
    }
}

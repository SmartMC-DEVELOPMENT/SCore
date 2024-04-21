package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.instance.BackendUTFListener;
import us.smartmc.backend.protocol.CommandRequest;

public class CommandHandlerListener extends BackendObjectListener<CommandRequest> {

    public CommandHandlerListener() {
        super(CommandRequest.class);
    }

    @Override
    public void onReceive(ConnectionHandler connection, CommandRequest object) {
        ConnectionInputManager.performCommand(connection, object);
    }
}

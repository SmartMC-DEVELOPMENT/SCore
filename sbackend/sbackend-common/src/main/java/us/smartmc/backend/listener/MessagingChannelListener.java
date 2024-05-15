package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.MessagingChannelsManager;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.instance.messaging.MessageCommand;

public class MessagingChannelListener extends BackendObjectListener<MessageCommand> {

    public MessagingChannelListener() {
        super(MessageCommand.class);
    }

    @Override
    public void onReceive(ConnectionHandler connection, MessageCommand command) {
        MessagingChannelsManager.perform(command.getId(), command.getMessage());
    }
}

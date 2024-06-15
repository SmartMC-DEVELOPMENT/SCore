package us.smartmc.backend.listener;

import us.smartmc.backend.connection.BackendClientConnection;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.instance.messaging.MessageCommand;

public class SubscriptionsListeners extends BackendObjectListener<MessageCommand> {

    @Override
    public void onReceive(ConnectionHandler connection, MessageCommand messageCommand) {
        String channelId = messageCommand.getId();
        BackendClientConnection.forEachBackendClient(client -> {
            if (!client.isChannelSubscriptorOf(channelId)) return;
            client.getConnectionHandler().sendObject(messageCommand);
        });
    }
}

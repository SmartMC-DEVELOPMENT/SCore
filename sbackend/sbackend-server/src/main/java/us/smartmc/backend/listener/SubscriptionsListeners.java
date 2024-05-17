package us.smartmc.backend.listener;

import us.smartmc.backend.connection.BackendClientConnection;
import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.instance.messaging.MessageCommand;

public class SubscriptionsListeners extends BackendObjectListener<MessageCommand> {

    public SubscriptionsListeners() {
        super(MessageCommand.class);
    }

    @Override
    public void onReceive(ConnectionHandler connection, MessageCommand messageCommand) {
        String channelId = messageCommand.getId();
        System.out.println("Broadcasting message " + channelId + " '" + messageCommand.getMessage() + "'");
        BackendClientConnection.forEachBackendClient(client -> {
            if (!client.isSuscriptorOf(channelId)) return;
            client.getConnectionHandler().sendObject(messageCommand);
        });
    }
}

package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.handler.PlayerContextsHandler;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.protocol.FeedbackObjectResponse;
import us.smartmc.backend.protocol.FeedbackObjectsRequest;
import us.smartmc.backend.protocol.ObjectsTransfer;
import us.smartmc.backend.protocol.UTFMessage;

import java.util.Map;
import java.util.UUID;

public class PlayerContextsListeners implements BackendObjectListener {

    @Override
    public void onReceive(ConnectionHandler connection, Object object) {
        if (object instanceof ObjectsTransfer transfer) {
            Object initialObject = transfer.getObjects()[0];
            System.out.println("InitialObject is " + initialObject);
            if (initialObject.equals("SETPLAYERCACHE")) {
                UUID id = (UUID) transfer.getObjects()[1];
                String key = (String) transfer.getObjects()[2];
                Object value = transfer.getObjects()[3];
                PlayerContextsHandler.setCache(id, key, value);
            } else if (initialObject.equals("REMOVEPLAYERCACHE")) {
                UUID id = (UUID) transfer.getObjects()[1];
                PlayerContextsHandler.remove(id);
            }
        }

        if (object instanceof UTFMessage message) {
            String initialObject = message.getMessage();
            if (initialObject.startsWith("GETPLAYERCACHEKEY ")) {
                String[] args = initialObject.split(" ");
                UUID id = UUID.fromString(args[1]);
                String key = String.valueOf(args[2]);
                connection.sendObject(new UTFMessage("HERE KEY " + id + " " + key));
            }
        }

    }
}

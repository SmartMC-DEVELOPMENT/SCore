package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.protocol.FeedbackObjectResponse;
import us.smartmc.backend.protocol.FeedbackObjectsRequest;

public class FeedbackObjectListeners implements BackendObjectListener {

    @Override
    public void onReceive(ConnectionHandler connection, Object object) {
        if (!(object instanceof FeedbackObjectResponse response)) return;
        FeedbackObjectsRequest.complete(response.getId(), response.getObjects());
    }
}

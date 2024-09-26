package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.gamescore.CuboidSaveRequest;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;

public class SaveCuboidListener extends BackendObjectListener<CuboidSaveRequest> {

    @Override
    public void onReceive(ConnectionHandler handler, CuboidSaveRequest request) {
        String name = request.getName();
        CuboidWrapper wrapper = request.getWrapper();

    }
}

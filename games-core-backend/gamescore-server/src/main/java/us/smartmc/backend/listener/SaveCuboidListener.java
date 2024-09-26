package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.gamescore.CuboidSaveRequest;
import us.smartmc.backend.gamescore.CuboidSaveResponse;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.util.CuboidSerializer;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;

import java.io.File;

public class SaveCuboidListener extends BackendObjectListener<CuboidSaveRequest> {

    @Override
    public void onReceive(ConnectionHandler handler, CuboidSaveRequest request) {
        String name = request.getName();
        CuboidWrapper wrapper = request.getWrapper();
        try {
            File toFile = new File(CuboidSerializer.getRegionsDir(), name + ".reg");
            CuboidSerializer.serialize(wrapper, toFile);
            handler.sendObject(new CuboidSaveResponse(name, CuboidSaveResponse.RequestResponse.OK));
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendObject(new CuboidSaveResponse(name, CuboidSaveResponse.RequestResponse.ERROR));
        }
    }
}

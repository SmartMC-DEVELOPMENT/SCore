package us.smartmc.backend.listener;

import us.smartmc.backend.connection.ConnectionHandler;
import us.smartmc.backend.gamescore.CuboidGetRequest;
import us.smartmc.backend.gamescore.CuboidGetResponse;
import us.smartmc.backend.instance.BackendObjectListener;
import us.smartmc.backend.manager.CacheCuboidManager;
import us.smartmc.backend.util.CuboidSerializer;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;

import java.io.File;

public class GetCuboidListener extends BackendObjectListener<CuboidGetRequest> {

    @Override
    public void onReceive(ConnectionHandler handler, CuboidGetRequest request) {
        String name = request.getName();
        CuboidWrapper wrapper = CacheCuboidManager.get(name);

        if (wrapper != null) {
            handler.sendObject(new CuboidGetResponse(name, wrapper));
            return;
        }

        try {
            File fromFile = new File(CuboidSerializer.getRegionsDir(), name + ".reg");
            wrapper = CuboidSerializer.deserialize(fromFile);
            handler.sendObject(new CuboidGetResponse(name, wrapper));
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendObject(new CuboidGetResponse(name, null));
        }
    }
}

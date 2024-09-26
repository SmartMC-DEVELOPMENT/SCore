package us.smartmc.backend.gamescore;

import us.smartmc.gamescore.instance.cuboid.Cuboid;
import us.smartmc.gamescore.instance.serialization.CuboidWrapper;
import us.smartmc.gamescore.instance.serialization.CuboidWrapperBuilder;

import java.io.Serializable;

public class CuboidSaveRequest implements Serializable {

    private final String name;
    private final CuboidWrapper wrapper;

    public CuboidSaveRequest(String name, Cuboid cuboid) {
        this.name = name;
        this.wrapper = new CuboidWrapperBuilder(cuboid).getWrapper();
    }

    public String getName() {
        return name;
    }

    public CuboidWrapper getWrapper() {
        return wrapper;
    }
}

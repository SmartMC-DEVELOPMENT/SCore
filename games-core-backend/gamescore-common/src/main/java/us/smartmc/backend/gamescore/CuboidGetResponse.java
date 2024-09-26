package us.smartmc.backend.gamescore;

import us.smartmc.gamescore.instance.serialization.CuboidWrapper;

import java.io.Serializable;

public class CuboidGetResponse implements Serializable {

    private final String name;
    private final CuboidWrapper wrapper;

    public CuboidGetResponse(String name, CuboidWrapper wrapper) {
        this.name = name;
        this.wrapper = wrapper;
    }

    public String getName() {
        return name;
    }

    public CuboidWrapper getWrapper() {
        return wrapper;
    }

}

package us.smartmc.backend.gamescore;

import us.smartmc.gamescore.instance.serialization.CuboidWrapper;

import java.io.Serializable;

public class CuboidSaveRequest implements Serializable {

    @Getter
    private final CuboidWrapper wrapper;

    public CuboidSaveRequest(CuboidWrapper wrapper) {
        this.wrapper = wrapper;
    }

}

package us.smartmc.backend.gamescore;

import java.io.Serializable;

public class CuboidGetRequest implements Serializable {

    private final String name;

    public CuboidGetRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

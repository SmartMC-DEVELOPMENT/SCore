package us.smartmc.backend.gamescore;

import java.io.Serializable;

public class CuboidSaveResponse implements Serializable {

    private final String name;
    private final RequestResponse response;

    private CuboidSaveResponse(String name, RequestResponse response) {
        this.name = name;
        this.response = response;
    }

    public String getName() {
        return name;
    }

    public RequestResponse getResponse() {
        return response;
    }

    public enum RequestResponse implements Serializable {
        OK, ERROR
    }

}

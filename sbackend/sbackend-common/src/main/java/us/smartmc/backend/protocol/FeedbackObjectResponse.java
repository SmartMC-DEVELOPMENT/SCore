package us.smartmc.backend.protocol;

import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class FeedbackObjectResponse implements Serializable {

    @Getter
    private final UUID id;
    private final Object[] objects;

    @Getter
    private transient IFeedbackResponse<?> action;

    public FeedbackObjectResponse(UUID id, Object... initObjects) {
        this.id = id;
        this.objects = initObjects;
    }
}

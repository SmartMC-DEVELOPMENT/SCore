package us.smartmc.backend.protocol;

import lombok.Getter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class FeedbackObjectsRequest implements Serializable {

    private static final Map<UUID, FeedbackObjectsRequest> toComplete = new HashMap<>();

    @Getter
    private final UUID id = UUID.randomUUID();
    private final Object[] objects;

    @Getter
    private transient IFeedbackResponse<?> action;

    public FeedbackObjectsRequest(Object... initObjects) {
        this.objects = initObjects;
    }

    public FeedbackObjectsRequest whenComplete(IFeedbackResponse<?> action) {
        this.action = action;
        toComplete.put(id, this);
        return this;
    }

    private void complete(Object... response) {
        action.whenReceived(this, response);
        toComplete.remove(id);
    }

    public static FeedbackObjectsRequest of(Object... args) {
        return new FeedbackObjectsRequest(args);
    }

    public static void complete(UUID id, Object... args) {
        toComplete.get(id).complete(args);
    }

}

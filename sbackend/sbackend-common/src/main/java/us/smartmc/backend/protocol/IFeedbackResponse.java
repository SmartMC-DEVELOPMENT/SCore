package us.smartmc.backend.protocol;

public interface IFeedbackResponse<T extends FeedbackObjectsRequest> {

    void whenReceived(FeedbackObjectsRequest request, Object... args);

}

package us.smartmc.backend.instance.messaging;

public interface IMessageChannel {

    void onMessage(String channelId, String message);

}

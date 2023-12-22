package us.smartmc.core.pluginsapi.instance.handler;

public interface IRedisPubSubListener {

    void onMessage(String message);

    String getChannel();
}

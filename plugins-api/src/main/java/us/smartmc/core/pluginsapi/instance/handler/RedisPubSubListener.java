package us.smartmc.core.pluginsapi.instance.handler;

import redis.clients.jedis.JedisPubSub;

public abstract class RedisPubSubListener extends JedisPubSub implements IRedisPubSubListener {

    protected final String channel;

    public RedisPubSubListener(String channel) {
        this.channel = channel;
    }

    @Override
    public void onMessage(String channel, String message) {
        onMessage(message);
    }

    public String getChannel() {
        return channel;
    }
}

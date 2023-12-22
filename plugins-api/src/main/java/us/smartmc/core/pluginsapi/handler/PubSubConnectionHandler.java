package us.smartmc.core.pluginsapi.handler;

import us.smartmc.core.pluginsapi.connection.RedisConnection;
import us.smartmc.core.pluginsapi.instance.handler.IRedisPubSubListener;
import us.smartmc.core.pluginsapi.instance.handler.RedisPubSubListener;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;

public class PubSubConnectionHandler extends JedisPubSub {
    
    private static final HashMap<String, IRedisPubSubListener> mapListeners = new HashMap<>();

    public static void register(RedisPubSubListener... listeners) {
        for (RedisPubSubListener listener : listeners) {
            try(Jedis jedis = new Jedis(RedisConnection.mainConnection.getHost(), RedisConnection.mainConnection.getPort())) {
                new Thread(() -> {jedis.subscribe(listener, listener.getChannel());}).start();
            }
        }
    }

    @Override
    public void onMessage(String channel, String message) {
        if (!mapListeners.containsKey(channel)) return;
        mapListeners.get(channel).onMessage(message);
    }
}

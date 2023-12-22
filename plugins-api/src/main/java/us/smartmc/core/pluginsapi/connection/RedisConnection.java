package us.smartmc.core.pluginsapi.connection;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConnection extends JedisPool {

    public static RedisConnection mainConnection;

    private Jedis jedis;
    private final String host;
    private final int port;

    public RedisConnection(String host, int port) {
        super(buildPoolConfig(), host, port);
        System.out.println("Connecting to redis " + host + ":" + port);
        this.host = host;
        this.port = port;
        if (mainConnection != null) {
            mainConnection = this;
        }
    }

    public void send(String channel, String message) {
        try (Jedis jedis = getResource()) {
            jedis.publish(channel, message);
        }
    }

    @Override
    public Jedis getResource() {
        if (jedis == null) {
            jedis = new Jedis(host, port);
        }
        return jedis;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    private static JedisPoolConfig buildPoolConfig() {
        return new JedisPoolConfig();
    }
}

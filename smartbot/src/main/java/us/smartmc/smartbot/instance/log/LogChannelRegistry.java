package us.smartmc.smartbot.instance.log;

import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import redis.clients.jedis.JedisPubSub;
import us.smartmc.smartbot.SmartBotMain;
import us.smartmc.smartbot.connection.RedisConnection;

public abstract class LogChannelRegistry extends JedisPubSub implements Runnable, ILogChannelRegistry {

    private final String id;

    private final String redisChannelName;

    public LogChannelRegistry(String id, String redisChannelName) {
        this.id = id;
        this.redisChannelName = redisChannelName;
    }

    @Override
    public void run() {
        new Thread(() -> {
            RedisConnection.mainConnection.getResource().subscribe(this, redisChannelName);
        }).start();
    }

    @Override
    public void onMessage(String channel, String message) {
        onLogRegistryReceive(getChannel(), message);
    }

    public GuildChannel getChannel() {
        return SmartBotMain.getMainGuild().getGuildChannelById(id);
    }

    public String getRedisChannelName() {
        return redisChannelName;
    }

    public String getId() {
        return id;
    }
}

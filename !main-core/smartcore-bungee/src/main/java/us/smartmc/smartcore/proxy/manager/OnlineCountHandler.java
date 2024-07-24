package us.smartmc.smartcore.proxy.manager;

import lombok.Getter;
import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.manager.BungeeCordPluginsAPI;
import us.smartmc.bmotd.BMotdBungeeCord;
import us.smartmc.smartcore.proxy.SmartCoreBungeeCord;

import java.util.Timer;
import java.util.TimerTask;

public class OnlineCountHandler {

    private static final SmartCoreBungeeCord core = SmartCoreBungeeCord.getPlugin();

    private static final String KEY_PREFIX = "proxyCount.";
    private static TimerTask timerTask;
    private static Timer timer;

    @Getter
    private static int count;

    private static int lastSentCount;

    public static void startTask() {
        if (timerTask != null || timer != null) {
            timer.cancel();
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                update();
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 500, 1000);
    }

    public static void unregister() {
        RedisConnection.mainConnection.getResource().del(getCountKey());
    }

    public static void update() {
        int count = BungeeCordPluginsAPI.proxy.getOnlineCount();
        if (lastSentCount != count) {
            RedisConnection.mainConnection.getResource().set(getCountKey(), String.valueOf(count));
            lastSentCount = count;
        }
        countOnlinePlayers();
    }

    public static void countOnlinePlayers() {
        int total = 0;
        for (String key : RedisConnection.mainConnection.getResource().keys(getKeyPattern())) {
            try {
                total += Integer.parseInt(RedisConnection.mainConnection.getResource().get(key));
            } catch (Exception ignore) {
            }
        }
        count = total;
        BMotdBungeeCord.getInstance().getMotdManager().setOnlineCount(count);
    }

    public static int getOnlineCount() {
        return count;
    }

    public static String getCountKey() {
        return KEY_PREFIX + core.getProxyID();
    }

    public static String getKeyPattern() {
        return KEY_PREFIX + "*";
    }

}

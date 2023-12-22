package us.smartmc.core.variables;

import us.smartmc.core.SmartCore;
import us.smartmc.core.pluginsapi.connection.RedisConnection;
import us.smartmc.core.pluginsapi.instance.VariableListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CountVariables extends VariableListener<Player> {

    private static final HashMap<String, Long> counts = new HashMap<>();

    private static int lastPushed = -1;

    public CountVariables() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(SmartCore.getPlugin(), () -> {
            updateCount();
            for (String key : RedisConnection.mainConnection.getResource().keys("online.*")) {
                counts.put(key, Long.parseLong(RedisConnection.mainConnection.getResource().get(key)));
            }
            deleteUnregisteredCacheServers();
        }, 10, 20);
    }

    private static void deleteUnregisteredCacheServers() {
        for (String key : counts.keySet()) {
            boolean exists = RedisConnection.mainConnection.getResource().exists(key);
            if (exists) continue;
            counts.remove(key);
        }
    }

    public static void removeCacheCount() {
        String key = "online." + SmartCore.getServerID();
        RedisConnection.mainConnection.getResource().del(key);
    }

    public static String getCountOf(String idName) {
        String path = "online." + idName;

        int online = 0;
        for (String key : counts.keySet()) {
            if (key.startsWith(path) || key.equalsIgnoreCase(path)) {
                online += counts.get(key);
            }
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat format = new DecimalFormat("#,###", symbols);
        return format.format(online);
    }

    public static String getAllCount() {
        long count = 0;
        for (long amount : counts.values()) {
            count += amount;
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat format = new DecimalFormat("#,###", symbols);
        return format.format(count);
    }

    private void updateCount() {
        int count = Bukkit.getOnlinePlayers().size();

        if (lastPushed == count) return;

        String key = "online." + SmartCore.getServerID();
        RedisConnection.mainConnection.getResource().set(key, String.valueOf(count));
        //RedisConnection.mainConnection.getResource().expire(key, 33);
        lastPushed = count;
    }

    @Override
    public String parse(String message) {
        if (!message.contains("<count.")) return message;

        for (String arg : message.split(" ")) {
            if (arg.contains("<count.")) {
                arg = ChatColor.stripColor(arg.replace("&", "§"));
                String idName = arg.replace("<count.", "").replace(">", "");
                if (idName.equals("all")) {
                    message = message.replaceFirst(arg, getAllCount());
                } else {
                    message = message.replaceFirst(arg, getCountOf(idName));
                }
            }
        }
        return message;
    }

    @Override
    public String parse(Player player, String message) {
        return parse(message);
    }

}

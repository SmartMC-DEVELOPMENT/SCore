package us.smartmc.core.variables;

import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CountVariables extends VariableListener<Player> {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static final HashMap<String, Integer> serverCounts = new HashMap<>();
    public static final Map<String, Integer> proxyCounts = new HashMap<>();

    private static boolean uploadedMax = false;
    private static int lastPushed = -1;

    public CountVariables() {

        Runnable runnable = () -> {
            if (!uploadedMax) {
                RedisConnection.mainConnection.getResource().set("maxSlots." + SmartCore.getServerName(), String.valueOf(Bukkit.getMaxPlayers()));
                uploadedMax = true;
            }
            updateCount();

            // Server counts (Only if registering)
            for (String key : RedisConnection.mainConnection.getResource().keys("online.*")) {
                serverCounts.put(key, Integer.parseInt(RedisConnection.mainConnection.getResource().get(key)));
            }

            // Proxy counts
            for (String key : RedisConnection.mainConnection.getResource().keys("proxyCount.*")) {
                proxyCounts.put(key, Integer.parseInt(RedisConnection.mainConnection.getResource().get(key)));
            }
            deleteUnregisteredCacheServers();
        };
        scheduler.scheduleAtFixedRate(runnable, 1, 2, TimeUnit.SECONDS);
    }

    private static void deleteUnregisteredCacheServers() {
        for (String key : new HashSet<>(serverCounts.keySet())) {
            if (RedisConnection.mainConnection.getResource().exists(key)) continue;
            serverCounts.remove(key);
        }
    }

    public static String getCountOf(String idName) {
        String path = idName;
        if (!path.startsWith("online.")) path = "online." + idName;
        int online = 0;
        for (String key : serverCounts.keySet()) {
            if (key.startsWith(path) || key.equalsIgnoreCase(path)) {
                online += serverCounts.get(key);
            }
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat format = new DecimalFormat("#,###", symbols);
        return format.format(online);
    }

    public static String getAllCount(Collection<Integer> count) {
        long online = 0;
        for (Integer onlineCount : count) {
            online += onlineCount;
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        DecimalFormat format = new DecimalFormat("#,###", symbols);
        return format.format(online);
    }

    public static List<String> getKeysByPrefix(String prefix) {
        return serverCounts.keySet().stream()
                .filter(key -> key.startsWith("online." + prefix))
                .map(s -> s.replaceFirst("online.", ""))
                .sorted(Comparator.comparingInt(CountVariables::extractNumber))
                .collect(Collectors.toList());
    }

    private static int extractNumber(String str) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return -1;
    }

    private void updateCount() {
        int count = Bukkit.getOnlinePlayers().size();

        if (lastPushed == count) return;

        String key = "online." + SmartCore.getServerName();
        RedisConnection.mainConnection.getResource().set(key, String.valueOf(count));
        RedisConnection.mainConnection.getResource().set("maxSlots." + SmartCore.getServerName(), String.valueOf(Bukkit.getMaxPlayers()));
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
                    message = message.replaceFirst(arg, getAllCount(proxyCounts.values()));
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

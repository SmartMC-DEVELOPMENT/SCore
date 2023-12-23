package us.smartmc.core.variables;

import me.imsergioh.pluginsapi.connection.RedisConnection;
import me.imsergioh.pluginsapi.instance.VariableListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.smartmc.core.SmartCore;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class CountVariables extends VariableListener<Player> {

    public static final HashMap<String, Long> counts = new HashMap<>();

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
        for (String key : new HashSet<>(counts.keySet())) {
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
        String path = idName;
        if (!path.startsWith("online.")) path = "online." + idName;
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

    public static List<String> getKeysByPrefix(String prefix) {
        return counts.keySet().stream()
                .filter(key -> key.startsWith("online." + prefix))
                .map(s -> s.replaceFirst("online.", ""))
                .sorted(Comparator.comparingInt(CountVariables::extractNumber))
                .collect(Collectors.toList());
    }

    private static int extractNumber(String str) {
        // Encuentra el número al final de la cadena
        int numberStart = str.lastIndexOf('-') + 1;
        if (numberStart > 0 && numberStart < str.length()) {
            String numberPart = str.substring(numberStart);
            try {
                return Integer.parseInt(numberPart);
            } catch (NumberFormatException e) {
                // Si no es un número, retorna un valor alto para que vaya al final
                return Integer.MAX_VALUE;
            }
        }
        return Integer.MAX_VALUE; // En caso de que no haya número
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

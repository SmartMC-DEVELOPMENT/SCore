package us.smartmc.lobbymodule.handler;

import me.imsergioh.pluginsapi.connection.RedisConnection;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MaxSlotsInfoManager {

    private static final HashMap<String, Integer> maxSlots = new HashMap<>();

    public static int getMaxSlotsOf(String serverIdName) {
        if (maxSlots.containsKey(serverIdName)) return maxSlots.get(serverIdName);
        String str = RedisConnection.mainConnection.getResource().get("maxSlots." + serverIdName);
        int max = 0;
        try {
            max = Integer.parseInt(str);
        } catch (Exception e) {System.out.println("Could not found maxSlots of " + serverIdName);}
        maxSlots.put(serverIdName, max);
        return max;
    }

    public static int getMaxSlotsOfServers(String serverPrefix) {
        int online = 0;
        boolean isAlreadyCached = false;
        for (String key : maxSlots.keySet()) {
            if (key.contains(serverPrefix)) {
                isAlreadyCached = true;
                break;
            }
        }

        Set<String> keys = isAlreadyCached ? maxSlots.keySet().stream().filter(s -> s.contains(serverPrefix)).collect(Collectors.toSet()) :
                RedisConnection.mainConnection.getResource().keys("maxSlots." + serverPrefix + "*");

        for (String key : keys) {
            // Put into cache from redis
            if (!maxSlots.containsKey(key)) {
                String value = RedisConnection.mainConnection.getResource().get(key);
                int onlineFromKey = Integer.parseInt(value);
                maxSlots.put(getFixedKey(key), Integer.parseInt(value));
                online += onlineFromKey;
                continue;
            }
            online += maxSlots.get(key);
        }

        return online;
    }

    private static String getFixedKey(String key) {
        if (key.startsWith("maxSlots.")) return key.split("\\.")[1];
        return key;
    }

}

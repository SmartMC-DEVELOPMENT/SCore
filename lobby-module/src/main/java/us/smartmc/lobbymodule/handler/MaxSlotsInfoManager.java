package us.smartmc.lobbymodule.handler;

import me.imsergioh.pluginsapi.connection.RedisConnection;

import java.util.HashMap;

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

}

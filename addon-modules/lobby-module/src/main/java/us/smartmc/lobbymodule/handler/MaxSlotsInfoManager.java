package us.smartmc.lobbymodule.handler;

import us.smartmc.serverhandler.ServerHandlerBukkit;

import java.util.HashMap;

public class MaxSlotsInfoManager {

    private static final HashMap<String, Integer> maxSlots = new HashMap<>();

    public static int getMaxSlotsOf(String serverIdName) {
        if (maxSlots.containsKey(serverIdName)) return maxSlots.get(serverIdName);
        ServerHandlerBukkit.getClient().getCache("maxSlots." + serverIdName, cacheResult -> {
            int max = cacheResult.getOfClass(Integer.class);
            maxSlots.put(serverIdName, max);
        });
        if (!maxSlots.containsKey(serverIdName)) {
            System.out.println("Could not find " + serverIdName);
            return -1;
        }
        return maxSlots.get(serverIdName);
    }
}

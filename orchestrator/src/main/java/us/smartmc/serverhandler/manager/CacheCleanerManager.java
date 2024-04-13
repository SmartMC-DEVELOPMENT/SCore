package us.smartmc.serverhandler.manager;

import me.imsergioh.pluginsapi.connection.RedisConnection;

public class CacheCleanerManager {

    public static void checkServerCache() {
        for (String key : RedisConnection.mainConnection.getResource().keys("online.*")) {
            if (!ServerManager.isConnected(key.replace("online.", ""))) {
                removeServerCache(key);
                RedisConnection.mainConnection.getResource().del(key);
            }
        }
    }

    public static void removeServerCache(String name) {
        System.out.println("Removing server cache of -> " + name);
        for (String key : RedisConnection.mainConnection.getResource().keys("*." + name)) {
            RedisConnection.mainConnection.getResource().del(key);
        }
    }

}

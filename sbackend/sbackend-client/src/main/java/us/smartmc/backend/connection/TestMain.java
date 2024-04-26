package us.smartmc.backend.connection;

import redis.clients.jedis.JedisPool;
import us.smartmc.backend.connection.manager.PlayerCacheManager;
import us.smartmc.backend.protocol.GetPlayerCacheRequest;
import us.smartmc.backend.protocol.PlayerContextRequest;
import us.smartmc.backend.protocol.PlayerContextRequestType;

import java.util.UUID;

public class TestMain {

    private static BackendClient client;
    private static JedisPool pool;

    public static void main(String[] initArgs) throws Exception {

        //pool = new JedisPool("66.70.181.34", 6379);
        client = new BackendClient("66.70.181.34", 7723);
        client.login("default", "SmartMC2024Ñ");
        new Thread(client).start();

        UUID uuid = UUID.fromString("5f257be9-0c62-4b17-ab8a-4ad53f9acb44");
        while (true) {
            Thread.sleep(1000);
            getBackendCache(uuid);
        }
    }

    private static void set(UUID uuid, String key, Object value) {
        client.sendObject(
                new PlayerContextRequest(PlayerContextRequestType.SET_CACHE,
                        uuid,
                        key,
                        value));
    }

    /*private static void getRedisCache(UUID uuid) {
        System.out.println("Obteniendo de redis...");
        PlayerCacheManager.registerStartDate(1, uuid);
        try (Jedis jedis = pool.getResource()) {
            String value = jedis.get("testCache." + uuid);
            Map<?, ?> dataMap = new Gson().fromJson(value, Map.class);
            PlayerCache playerCache = new PlayerCache("testCache." + dataMap.get("_id"));
            PlayerCacheManager.parse(playerCache);
        }
    }*/

    private static void getBackendCache(UUID uuid) {
        System.out.println("Obteniendo de backend...");
        PlayerCacheManager.registerRequestDate();
        client.sendObject(new GetPlayerCacheRequest(uuid));
    }
}

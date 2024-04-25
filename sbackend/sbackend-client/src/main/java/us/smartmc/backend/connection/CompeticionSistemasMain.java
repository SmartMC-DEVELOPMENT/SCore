package us.smartmc.backend.connection;

import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import us.smartmc.backend.connection.manager.PlayerCacheManager;
import us.smartmc.backend.instance.player.PlayerCache;
import us.smartmc.backend.protocol.GetPlayerCacheRequest;
import us.smartmc.backend.protocol.PlayerContextRequest;
import us.smartmc.backend.protocol.PlayerContextRequestType;

import java.util.Map;
import java.util.UUID;

public class CompeticionSistemasMain {

    private static BackendClient client;
    private static JedisPool pool;

    public static void main(String[] initArgs) throws Exception {

        pool = new JedisPool("66.70.181.34", 6379);
        client = new BackendClient("66.70.181.34", 7723);
        client.login("default", "SmartMC2024");
        new Thread(client).start();

        boolean backend = true;
        while (true) {
            Thread.sleep(2000);
            UUID uuid = UUID.fromString("5f257be9-0c62-4b17-ab8a-4ad53f9acb44");
            if (backend) {
                getBackendCache(uuid);
                backend = false;
            } else {
                getRedisCache(uuid);
                backend = true;
            }
        }
    }

    private static void set(UUID uuid, String key, Object value) {
        client.sendObject(
                new PlayerContextRequest(PlayerContextRequestType.SET_CACHE,
                        uuid,
                        key,
                        value));
    }

    private static void getRedisCache(UUID uuid) {
        PlayerCacheManager.registerStartDate(1, uuid);
        try (Jedis jedis = pool.getResource()) {
            String value = jedis.get("testCache." + uuid);
            Map<?, ?> dataMap = new Gson().fromJson(value, Map.class);
            PlayerCache playerCache = new PlayerCache(UUID.fromString((String) dataMap.get("_id")));
            PlayerCacheManager.parse(playerCache);
        }
    }

    private static void getBackendCache(UUID uuid) {
        client.sendObject(new GetPlayerCacheRequest(uuid));
        PlayerCacheManager.registerStartDate(0, uuid);
    }
}

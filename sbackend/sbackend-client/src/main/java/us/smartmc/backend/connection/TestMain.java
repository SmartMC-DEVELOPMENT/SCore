package us.smartmc.backend.connection;

import redis.clients.jedis.JedisPool;

public class TestMain {

    private static BackendClient client;
    private static JedisPool pool;

    public static void main(String[] initArgs) throws Exception {
        client = new BackendClient("66.70.181.34", 7723);
        client.login("default", "SmartMC2024Ñ");
        new Thread(client).start();

        client.registerCache("player.imsergioh", "MISSAWOWA");
        client.getCache("player.imsergioh");
        client.registerCache("player.imsergioh", "VALOOOOR NUEVO PERO NO SE DEBERIA REMPLZAZAR");
        client.removeCache("PIMPARAPUM");
        client.removeCache("player.imsergioh");

        client.registerCache("player.imsergioh", "DIOOOOOS");
        client.registerCache("player.imsergioh", "123");
        client.getCache("player.imsergioh");
    }
}

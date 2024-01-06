import me.imsergioh.pluginsapi.connection.RedisConnection;

public class Main {

    public static void main(String[] args) {
        RedisConnection.mainConnection = new RedisConnection("smart.dedi1", 6379);
        System.out.println(RedisConnection.mainConnection.getResource().get("cooldown.friend.5f257be9-0c62-4b17-ab8a-4ad53f9acb44.5f257be9-0c62-4b17-ab8a-4ad53f9acb44").getClass().getName());
    }

}

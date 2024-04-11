package us.smartmc.backend.connection;

import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;
import us.smartmc.backend.connection.command.SetPlayerDataCmd;
import us.smartmc.backend.handler.ConnectionInputManager;
import us.smartmc.backend.instance.BackendUTFListener;

import java.util.HashMap;
import java.util.UUID;

public class TestMainMongo implements BackendUTFListener {

    public static void main(String[] initArgs) throws Exception {
        long start = System.currentTimeMillis();
        System.out.println("START " + start);
        TestMainMongo main = new TestMainMongo();
        ConnectionInputManager.registerListeners(main);
        ConnectionInputManager.registerCommands(new SetPlayerDataCmd());

        MongoDBConnection.mainConnection = new MongoDBConnection("66.70.181.34", 27017);

        UUID id = UUID.fromString("5f257be9-0c62-4b17-ab8a-4ad53f9acb44");
        Document document = MongoDBConnection.mainConnection.getDatabase("player_data")
                .getCollection("core_players").find(new org.bson.Document("_id", id.toString())).first();
        if (document == null) {
            System.out.println("No se ha podido cachear el data de sergio");
            return;
        }
        HashMap<String, Object> playerCache = new HashMap<>();
        for (String key : document.keySet()) {
            playerCache.put(key, document.get(key));
        }
        long end = System.currentTimeMillis();
        System.out.println(end+ " ENDED!");
    }

    @Override
    public void onReceive(ConnectionHandler connection, String utf) {
        long end = System.currentTimeMillis();
        System.out.println("RESPONSE! " + end + "(" + utf + ")");
    }
}

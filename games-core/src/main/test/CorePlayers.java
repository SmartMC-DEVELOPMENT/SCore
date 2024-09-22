import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;
import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.instance.storage.MongoDBData;
import us.smartmc.gamescore.manager.PlayerStatisticsManager;

import java.util.UUID;

public class CorePlayers {

    private static final TestStat stat = new TestStat();

    public static void main(String[] args) {
        MongoDBConnection.mainConnection = new MongoDBConnection("localhost", 27017);
        testStats();
        UUID id = UUID.fromString("c7332d8a-7481-4a49-a10f-b30fcd30098f");
        GameCorePlayer gameCorePlayer = new GameCorePlayer(id);
        gameCorePlayer.getStats().loadFromDatabase("test_db", "test_collection");
        System.out.println("TEST STAT = " + stat.getValue(gameCorePlayer));
        gameCorePlayer.getStats().loadFromDatabase("test_db", "new_collection");
        System.out.println("DATA = " + gameCorePlayer.getStats().getDataDocument().toString());
        gameCorePlayer.getStats().saveToDatabase(false, "test_db", "brand_collection");
    }

    private static void testStats() {
        PlayerStatisticsManager manager = PlayerStatisticsManager.getManager(PlayerStatisticsManager.class);
        if (manager == null) return;
        manager.register(stat);
    }

}

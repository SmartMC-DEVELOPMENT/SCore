import me.imsergioh.pluginsapi.connection.MongoDBConnection;
import org.bson.Document;
import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.instance.storage.MongoDBData;
import us.smartmc.gamescore.manager.PlayerStatisticsManager;

import java.util.UUID;

public class CorePlayers {

    private static final TestStat stat = new TestStat();

    public static void main(String[] args) {
        testStats();
        GameCorePlayer gameCorePlayer = new GameCorePlayer(UUID.randomUUID());
        for (int i = 0; i < 100; i++) {
            stat.decrease(gameCorePlayer);
        }
        System.out.println("STAT = " + stat.getValue(gameCorePlayer));
        System.out.println("DATA = " + gameCorePlayer.getStats().getDataDocument().toString());

        MongoDBConnection.mainConnection = new MongoDBConnection("localhost", 27017);

        MongoDBData data = new MongoDBData("test_db", "test_collection", new Document("_id", gameCorePlayer.getUUID().toString()));
        data.load();
        System.out.println("DATA=" + data);
        data.addAll(gameCorePlayer.getStats());
        data.save();
    }

    private static void testStats() {
        PlayerStatisticsManager manager = PlayerStatisticsManager.getManager(PlayerStatisticsManager.class);
        if (manager == null) return;
        manager.register(stat);
    }

}

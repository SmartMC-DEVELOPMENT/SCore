import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.manager.PlayerStatisticsManager;

import java.util.UUID;

public class CorePlayers {

    public static void main(String[] args) {
        testStats();

        GameCorePlayer gameCorePlayer = new GameCorePlayer(UUID.randomUUID());

        System.out.println("STAT = " + gameCorePlayer.getStats().get("test_number"));
        System.out.println("TYPEOF STAT = " + gameCorePlayer.getStats().get("test_number").getClass());
    }

    private static void testStats() {
        PlayerStatisticsManager manager = PlayerStatisticsManager.getManager(PlayerStatisticsManager.class);
        if (manager == null) return;
        manager.register(new TestStat());

    }

}

import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.manager.PlayerStatisticsManager;

import java.util.UUID;

public class CorePlayers {

    private static final TestStat stat = new TestStat();

    public static void main(String[] args) {
        testStats();
        GameCorePlayer gameCorePlayer = new GameCorePlayer(UUID.randomUUID());
        for (int i = 0; i < 100; i++) {
            stat.increase(gameCorePlayer);
        }
        System.out.println("STAT = " + stat.getValue(gameCorePlayer));
        System.out.println("DATA = " + gameCorePlayer.getStats().getDataDocument().toString());
    }

    private static void testStats() {
        PlayerStatisticsManager manager = PlayerStatisticsManager.getManager(PlayerStatisticsManager.class);
        if (manager == null) return;
        manager.register(stat);
    }

}

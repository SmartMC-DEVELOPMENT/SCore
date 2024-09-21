import instance.manager.TestSetManager;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.PlayersManager;

import java.util.UUID;

public class TestMain {

    public static void main(String[] args) {
        TestSetManager testManager = new TestSetManager();
        testManager.add(UUID.randomUUID());

        System.out.println("VALUE?" + testManager);

        PlayersManager playerManager = MapManager.getManager(PlayersManager.class);
        System.out.println(playerManager);
    }

}

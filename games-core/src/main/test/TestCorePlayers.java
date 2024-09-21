import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.instance.player.PlayerStatus;

import java.util.UUID;

public class TestCorePlayers {

    public static void main(String[] args) {
        GameCorePlayer gameCorePlayer = new GameCorePlayer(UUID.randomUUID());
        gameCorePlayer.setStatus(PlayerStatus.POST_GAME);
        System.out.println(gameCorePlayer.getStatus());
    }

}

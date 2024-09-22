package us.smartmc.gamescore.instance.game;

import org.bukkit.entity.Player;
import us.smartmc.gamescore.manager.GamesManager;

public interface IGame {

    void setStatus(GameStatus status);
    GameStatus getStatus();

    void joinPlayer(Player player);

    void killPlayer(Player player);

    void leavePlayer(Player player);

    default GamesManager getManager() {
        return GamesManager.getManager(GamesManager.class);
    }

}

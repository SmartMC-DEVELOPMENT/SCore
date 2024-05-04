package us.smartmc.core.randomwar.instance.game;

import org.bukkit.entity.Player;
import us.smartmc.core.randomwar.instance.player.GamePlayer;

import java.util.Set;

public interface IGameSession {

    void start();
    void end();

    boolean canStart();
    boolean canEnd();

    void joinPlayer(GamePlayer player);
    void quitPlayer(GamePlayer player);

    void deathPlayer(GamePlayer player);

    GameSessionTeams getTeams();
    GameMap getMap();
    Set<GamePlayer> getPlayers();

    default GamePlayer gamePlayer(Player player) {
        return GamePlayer.get(player.getUniqueId());
    }
}

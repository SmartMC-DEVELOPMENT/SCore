package us.smartmc.game.luckytowers.instance.game;

import org.bukkit.entity.Player;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;

import java.util.Collection;
import java.util.Set;

public interface IGameSession {

    void start();
    void end();

    boolean canStart();
    boolean canEnd();

    void joinPlayer(GamePlayer player);
    void quitPlayer(GamePlayer player);

    void deathPlayer(GamePlayer player);

    void setStatus(GameSessionStatus status);

    GameSessionTeams getTeams();
    GameMap getMap();
    Collection<Player> getAlivePlayers();
    Set<GamePlayer> getPlayers();

    GameSessionStatus getStatus();

    default GamePlayer gamePlayer(Player player) {
        return GamePlayer.get(player.getUniqueId());
    }
}

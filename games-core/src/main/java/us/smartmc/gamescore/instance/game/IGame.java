package us.smartmc.gamescore.instance.game;

import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.player.PlayerStatus;
import us.smartmc.gamescore.manager.GamesManager;

import java.util.Set;

public interface IGame {

    void setStatus(GameStatus status);
    GameStatus getStatus();

    void joinPlayer(Player player);
    void joinSpectatorViewer(Player player);

    void killPlayer(Player player);

    void leavePlayer(Player player);

    default Set<Player> getSpectators() {
        Set<Player> players = getPlayersByStatus(PlayerStatus.SPECTATOR_VIEWER);
        players.addAll(getPlayersByStatus(PlayerStatus.SPECTATOR_DEATH));
        return players;
    }

    Set<Player> getPlayersByStatus(PlayerStatus status);
    Set<Player> getPlayers();

    default GamesManager getManager() {
        return GamesManager.getManager(GamesManager.class);
    }

}

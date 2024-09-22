package us.smartmc.gamescore.manager;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.game.GameStatus;
import us.smartmc.gamescore.instance.game.IGame;
import us.smartmc.gamescore.instance.manager.SetManager;
import us.smartmc.gamescore.instance.player.GameCorePlayer;

import java.util.HashSet;
import java.util.Set;

@Getter
public class GamesManager extends SetManager<IGame> {

    public void unregisterPlayer(Player player) {
        forEach(game -> {
            game.leavePlayer(GameCorePlayer.of(player));
        });
    }

    public Set<IGame> getWaitingGames() {
        return getGamesByStatus(GameStatus.WAITING);
    }

    public Set<IGame> getStartingGames() {
        return getGamesByStatus(GameStatus.WAITING);
    }

    public Set<IGame> getPlayingGames() {
        return getGamesByStatus(GameStatus.PLAYING);
    }

    public Set<IGame> getEndingGames() {
        return getGamesByStatus(GameStatus.ENDING);
    }

    public Set<IGame> getGamesByStatus(GameStatus status) {
        Set<IGame> games = new HashSet<>();
        forEach(game -> {
           if (!game.getStatus().equals(status)) return;
           games.add(game);
        });
        return games;
    }

}

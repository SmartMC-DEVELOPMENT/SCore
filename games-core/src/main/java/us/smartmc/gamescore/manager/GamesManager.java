package us.smartmc.gamescore.manager;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.smartmc.gamescore.instance.game.Game;
import us.smartmc.gamescore.instance.game.GameStatus;
import us.smartmc.gamescore.instance.game.IGame;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.instance.player.GameCorePlayer;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class GamesManager extends MapManager<UUID, IGame> {

    public void unregisterPlayer(Player player) {
        forEach((id, game) -> {
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
        forEach((id, game) -> {
           if (!game.getStatus().equals(status)) return;
           games.add(game);
        });
        return games;
    }

    @Override
    public IGame createValueByKey(UUID id) {
        return new Game(id);
    }
}

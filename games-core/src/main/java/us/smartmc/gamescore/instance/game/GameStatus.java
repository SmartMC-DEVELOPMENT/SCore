package us.smartmc.gamescore.instance.game;

import us.smartmc.gamescore.manager.GamesManager;

import java.util.HashSet;
import java.util.Set;

public enum GameStatus {

    WAITING, STARTING, STARTING_FULL, IN_GAME, ENDING;

    GameStatus() {

    }

    public Set<IGame> getGames() {
        GamesManager manager = GamesManager.getManager(GamesManager.class);
        if (manager == null) return new HashSet<>();
        return manager.getGamesByStatus(this);
    }

}

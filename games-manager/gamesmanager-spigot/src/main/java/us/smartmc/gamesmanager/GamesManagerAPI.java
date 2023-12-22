package us.smartmc.gamesmanager;

import us.smartmc.gamesmanager.game.IGamePreset;

public interface GamesManagerAPI<T extends IGamePreset> {

    T getGamesManager();

}

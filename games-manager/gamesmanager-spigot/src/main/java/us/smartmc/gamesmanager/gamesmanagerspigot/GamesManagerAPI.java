package us.smartmc.gamesmanager.gamesmanagerspigot;

import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GameManager;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GamePlayerManager;

public interface GamesManagerAPI {

    GameManager<?> getGameManager();
    GamePlayerManager<?> getGamePlayerManager();

}

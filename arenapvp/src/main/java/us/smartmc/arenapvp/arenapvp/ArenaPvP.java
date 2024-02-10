package us.smartmc.arenapvp.arenapvp;

import us.smartmc.arenapvp.arenapvp.instance.ArenaGame;
import us.smartmc.arenapvp.arenapvp.instance.player.ArenaPlayer;
import us.smartmc.arenapvp.arenapvp.manager.ArenaPlayerManager;
import us.smartmc.gamesmanager.gamesmanagerspigot.GamesManagerAPI;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GameManager;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GamePlayerManager;

public class ArenaPvP extends GamesManagerAPI<ArenaGame, ArenaPlayer> {

    private GameManager<ArenaGame> gameManager;
    private GamePlayerManager<ArenaPlayer> playerManager;

    @Override
    public void onEnable() {
        gameManager = new GameManager<>();
        playerManager = new ArenaPlayerManager(this);
     }

    @Override
    public void onDisable() {
    }

    @Override
    public GameManager<?> getGameManager() {
        return gameManager;
    }

    @Override
    public GamePlayerManager<?> getGamePlayerManager() {
        return playerManager;
    }
}

package us.smartmc.gamesmanager.gamesmanagerspigot;

import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GameManager;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GamePlayerManager;

public interface IGamesManagerAPI<GameType extends GameInstance, PlayerType extends GamePlayer> {

    GameManager<GameType> getGameManager();
    GamePlayerManager<PlayerType> getGamePlayerManager();

}

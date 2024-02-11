package us.smartmc.gamesmanager.gamesmanagerspigot;

import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GameManager;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GamePlayerManager;

public interface IGamesManagerAPI {

    GameManager<?> getGameManager();
    GamePlayerManager<?> getGamePlayerManager();

}

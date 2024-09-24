package us.smartmc.gamescore.api;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.gamescore.cmd.RegionsCommand;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.instance.manager.SetManager;
import us.smartmc.gamescore.listener.PlayerGameLogicListeners;
import us.smartmc.gamescore.listener.PlayersManagerListeners;
import us.smartmc.gamescore.manager.GamesManager;
import us.smartmc.gamescore.manager.PlayersManager;

import java.util.logging.Logger;

public abstract class GamesCoreAPI implements IGamesCoreAPI {

    @Getter
    private static GamesCoreAPI api;

    private final JavaPlugin plugin;

    public GamesCoreAPI(JavaPlugin plugin) {
        this.plugin = plugin;
        api = this;
    }

    @Override
    public void initialize(JavaPlugin plugin) {
        try {
            registerListeners(new PlayerGameLogicListeners(), new PlayersManagerListeners());
        } catch (Exception e) {
            getLogger().severe("Error trying to register Listeners from default listeners package!");
            throw new RuntimeException(e);
        }
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }

    public static GamesManager getGamesManager() {
        return MapManager.getManager(GamesManager.class);
    }

    public static PlayersManager getPlayersManager() {
        return MapManager.getManager(PlayersManager.class);
    }
}

package us.smartmc.gamescore.api;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.backend.gamescore.BackendConnection;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.listener.PlayerGameLogicListeners;
import us.smartmc.gamescore.listener.PlayersManagerListeners;
import us.smartmc.gamescore.manager.GamesManager;
import us.smartmc.gamescore.manager.PlayersManager;

import java.io.IOException;

public abstract class GamesCoreAPI implements IGamesCoreAPI {

    @Getter
    private static GamesCoreAPI api;

    private final JavaPlugin plugin;

    @Getter
    private final BackendConnection backendConnection;

    public GamesCoreAPI(JavaPlugin plugin) {
        this.plugin = plugin;
        api = this;
        try {
            this.backendConnection = new BackendConnection("admin.smartmc.us", 7723, "default", "SmartMC2024Ñ");
            new Thread(backendConnection::run).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

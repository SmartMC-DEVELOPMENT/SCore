package us.smartmc.gamescore.api;

import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.manager.PlayersManager;

public abstract class GamesCoreAPI implements IGamesCoreAPI {

    private final JavaPlugin plugin;

    public GamesCoreAPI(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize(JavaPlugin plugin) {
        try {
            registerListeners("us.smartmc.gamescore.listener");
        } catch (Exception e) {
            getLogger().severe("Error trying to register Listeners from default listeners package!");
            throw new RuntimeException(e);
        }
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }

    public static PlayersManager getPlayersManager() {
        return MapManager.getManager(PlayersManager.class);
    }
}

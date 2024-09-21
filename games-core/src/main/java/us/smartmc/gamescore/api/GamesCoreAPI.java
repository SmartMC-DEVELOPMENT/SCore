package us.smartmc.gamescore.api;

import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.gamescore.listener.PlayersManagerListeners;

public abstract class GamesCoreAPI implements IGamesCoreAPI {

    private final JavaPlugin plugin;

    public GamesCoreAPI(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize(JavaPlugin plugin) {
        registerListeners(new PlayersManagerListeners());
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }
}

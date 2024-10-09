package us.smartmc.test;

import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.gamescore.api.GamesCoreAPI;

public class GameIntegration extends GamesCoreAPI {

    public GameIntegration(JavaPlugin plugin) {
        super("test_integration", plugin);
        initialize(plugin);
    }
}

package us.smartmc.gamescore.api;

import lombok.Getter;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.manager.ItemActionsManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.backend.gamescore.BackendConnection;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.itemcmd.AddCustomMetadataItemCMD;
import us.smartmc.gamescore.itemcmd.ListMetadataItemCMD;
import us.smartmc.gamescore.itemcmd.RemoveCustomMetadataItemCMD;
import us.smartmc.gamescore.itemcmd.ToggleMetadataItemCMD;
import us.smartmc.gamescore.listener.PlayerGameLogicListeners;
import us.smartmc.gamescore.listener.PlayersManagerListeners;
import us.smartmc.gamescore.listener.RegionsMetadataListeners;
import us.smartmc.gamescore.listener.TestCustomRegionMetadataListener;
import us.smartmc.gamescore.manager.GamesManager;
import us.smartmc.gamescore.manager.player.PlayersManager;
import us.smartmc.gamescore.manager.RegionsManager;

import java.io.IOException;
import java.util.Optional;

public abstract class GamesCoreAPI implements IGamesCoreAPI {

    @Getter
    private static GamesCoreAPI api;

    private final JavaPlugin plugin;

    @Getter
    private final BackendConnection backendConnection;

    public GamesCoreAPI(JavaPlugin plugin) {
        this.plugin = plugin;
        api = this;
        SpigotPluginsAPI.setup(plugin);
        try {
            this.backendConnection = new BackendConnection("admin.smartmc.us", 7723, "default", "SmartMC2024Ñ");
            new Thread(backendConnection).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ItemActionsManager.registerCommand("toggleRegionMetadata", new ToggleMetadataItemCMD());
        ItemActionsManager.registerCommand("addRegionMetadata", new AddCustomMetadataItemCMD());
        ItemActionsManager.registerCommand("removeRegionMetadata", new RemoveCustomMetadataItemCMD());

        ItemActionsManager.registerCommand("listRegionMetadata", new ListMetadataItemCMD());

        registerListeners(new TestCustomRegionMetadataListener());

        // Create at setup of API Regions Manager (to load regions, etc.)
        RegionsManager.getManager(RegionsManager.class);
    }

    @Override
    public void initialize(JavaPlugin plugin) {
        try {
            registerListeners(new PlayerGameLogicListeners(), new PlayersManagerListeners(), new RegionsMetadataListeners());
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

    public static Optional<GamesCoreAPI> getApiOptional() {
        return Optional.ofNullable(api);
    }
}

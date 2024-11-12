package us.smartmc.lobbymodule;

import lombok.Getter;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import me.imsergioh.pluginsapi.manager.ItemActionsManager;
import me.imsergioh.pluginsapi.util.LocationSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import us.smartmc.core.SmartCore;
import us.smartmc.lobbymodule.command.*;
import us.smartmc.lobbymodule.config.LobbyConfig;
import us.smartmc.lobbymodule.config.MinigamesConfig;
import us.smartmc.lobbymodule.handler.*;
import us.smartmc.lobbymodule.itemcommand.*;
import us.smartmc.lobbymodule.listener.*;
import us.smartmc.lobbymodule.messages.LobbyMessages;
import us.smartmc.lobbymodule.messages.MinigamesMessages;
import us.smartmc.smartaddons.plugin.AddonInfo;
import us.smartmc.smartaddons.plugin.AddonPlugin;

import java.io.File;

@AddonInfo(name = "lobby-module", version = "DEV")
public class LobbyModule extends AddonPlugin {

    @Getter
    private static AddonPlugin plugin;
    @Getter
    private static LobbyConfig lobbyConfig;
    @Getter
    private static MinigamesConfig minigamesConfig;

    @Getter
    private static SpigotYmlConfig lobbiesMenuConfig;

    @Getter
    private static LinkSocialsManager linkSocialsManager;

    @Getter
    private static SpigotYmlConfig mainConfig;

    @Override
    public void start() {

        plugin = this;
        log("Module starting...");

        lobbyConfig = new LobbyConfig(this);

        Bukkit.getScheduler().runTaskLater(SmartCore.getPlugin(), () -> {
            minigamesConfig = new MinigamesConfig();
        }, 20);

        mainConfig = new SpigotYmlConfig(new File(getDataFolder(), "config.yml"));
        mainConfig.set("top_parkour_location", LocationSerializer.toString(new Location(Bukkit.getWorlds().get(0), 28, 73, 45)));

        lobbiesMenuConfig = new SpigotYmlConfig(new File(SpigotPluginsAPI.getPlugin().getDataFolder() + "/menus", "lobbies.yml"));
        lobbiesMenuConfig.register("id_prefix", "main-lobby");
        lobbiesMenuConfig.register("item_name", "<lang.lobby.main-lobby.name>");
        lobbiesMenuConfig.save();
        LobbiesInfoManager.start();

        registerCommand(new LobbyCommand());
        registerListeners(SpigotPluginsAPI.getPlugin(), new DefaultConfigListeners(lobbyConfig));

        registerCommand(
                new FlyCommand("fly"),
                new FlyCommand("volar"),
                new FlyCommand("flight"),
                new LinkSocialsCommand("linkSocials"),
                new SocialsCommand("socials"));


        registerListeners(SpigotPluginsAPI.getPlugin(),
                new JoinItemListener(),
                new VisibilityManager(),
                new InventoryListeners(),
                new FlyManager(),
                new CustomJoinSlotListener(),
                new PlayerListener(),
                new LobbiesInfoManager(),
                new CancelListeners(),
                new PlatesListener(),
                new ParkourListeners(),
                new ConnectLobbyListener());
        registerCommand(new ChangeVisibilityCommand());

        new WorldConfigManager();
        linkSocialsManager = new LinkSocialsManager();
        linkSocialsManager.load();

        new LobbyMessages();
        new MinigamesMessages();

        getDataFolder().mkdirs();

        MaxYLocationManager.start();

        ItemActionsManager.registerCommand("toggleFly", new ToggleFlyAction());
        ItemActionsManager.registerCommand("minigamesExecutor", new MinigamesActions());
        ItemActionsManager.registerCommand("lobbyModule", new LobbyModuleAction());
        ItemActionsManager.registerCommand("connectTo", new ConnectToAction());
        ItemActionsManager.registerCommand("linkSocial", new LinkSocialAction());
        ItemActionsManager.registerCommand("showSocial", new ShowSocialAction());

        LobbiesInfoManager.registerDefaults();

        log("STARTED");
    }

    @Override
    public void stop() {
        MaxYLocationManager.cancelTask();
        log("Goodbye! :D");
    }

    public static void log(String message) {
        SmartCore.logger.info("[LOBBY-MODULE] " + message);
    }

}

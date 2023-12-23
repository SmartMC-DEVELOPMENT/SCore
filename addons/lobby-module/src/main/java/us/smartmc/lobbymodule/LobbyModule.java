package us.smartmc.lobbymodule;

import lombok.Getter;
import me.imsergioh.pluginsapi.SpigotPluginsAPI;
import me.imsergioh.pluginsapi.instance.SpigotYmlConfig;
import me.imsergioh.pluginsapi.manager.ItemActionsManager;
import us.smartmc.core.SmartCore;
import us.smartmc.lobbymodule.command.ChangeVisibilityCommand;
import us.smartmc.lobbymodule.command.FlyCommand;
import us.smartmc.lobbymodule.command.LobbyCommand;
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

    @Override
    public void start() {

        plugin = this;
        log("Module starting...");

        lobbyConfig = new LobbyConfig(this);
        minigamesConfig = new MinigamesConfig();

        lobbiesMenuConfig = new SpigotYmlConfig(new File(SpigotPluginsAPI.getPlugin().getDataFolder() + "/menus", "lobbies.yml"));
        lobbiesMenuConfig.register("id_prefix", "main-lobby");
        lobbiesMenuConfig.register("item_name", "<lang.lobby.main_lobby_name>");
        lobbiesMenuConfig.save();
        LobbiesInfoManager.start();

        registerCommand(new LobbyCommand());
        registerListeners(new DefaultConfigListeners(lobbyConfig));

        registerCommand(new FlyCommand("fly"));
        registerCommand(new FlyCommand("volar"));
        registerCommand(new FlyCommand("flight"));

        registerListeners(new JoinItemListener(),
                new VisibilityManager(),
                new InventoryListeners(),
                new FlyManager(),
                new CustomJoinSlotListener(),
                new PlayerListener(),
                new LobbiesInfoManager());
        registerCommand(new ChangeVisibilityCommand());

        new WorldConfigManager();

        new LobbyMessages();
        new MinigamesMessages();

        getDataFolder().mkdirs();

        MaxYLocationManager.start();

        log("STARTED");

        ItemActionsManager.registerCommand("alternateVisibility", new AlternateVisibilityAction());
        ItemActionsManager.registerCommand("toggleFly", new ToggleFlyAction());
        ItemActionsManager.registerCommand("minigamesExecutor", new MinigamesActions());
        ItemActionsManager.registerCommand("lobbyModule", new LobbyModuleAction());
        ItemActionsManager.registerCommand("connectTo", new ConnectToAction());
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

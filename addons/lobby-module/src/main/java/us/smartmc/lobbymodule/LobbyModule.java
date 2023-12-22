package us.smartmc.lobbymodule;

import lombok.Getter;
import us.smartmc.core.SmartCore;
import us.smartmc.core.pluginsapi.handler.ItemActionsManager;
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

@AddonInfo(name = "lobby-module", version = "DEV")
public class LobbyModule extends AddonPlugin {

    @Getter
    private static AddonPlugin plugin;
    @Getter
    private static LobbyConfig lobbyConfig;
    @Getter
    private static MinigamesConfig minigamesConfig;

    @Override
    public void start() {

        plugin = this;
        log("Module starting...");

        lobbyConfig = new LobbyConfig(this);
        minigamesConfig = new MinigamesConfig();

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
                new PlayerListener());
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

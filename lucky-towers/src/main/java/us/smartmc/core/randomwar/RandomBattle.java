package us.smartmc.core.randomwar;

import lombok.Getter;
import me.imsergioh.pluginsapi.language.EnumMessagesRegistry;
import me.imsergioh.pluginsapi.manager.ItemActionsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.core.randomwar.command.AdminGameCommand;
import us.smartmc.core.randomwar.command.LeaveCommand;
import us.smartmc.core.randomwar.config.MainPluginConfig;
import us.smartmc.core.randomwar.instance.game.GameTemplate;
import us.smartmc.core.randomwar.itemcmd.AdminEditorCommand;
import us.smartmc.core.randomwar.itemcmd.LobbyHotbarCommand;
import us.smartmc.core.randomwar.itemcmd.PlayerOptionCommand;
import us.smartmc.core.randomwar.listener.EssentialsListeners;
import us.smartmc.core.randomwar.listener.MainGameListeners;
import us.smartmc.core.randomwar.listener.PlayerLogicListeners;
import us.smartmc.core.randomwar.manager.GameMapManager;
import us.smartmc.core.randomwar.manager.GameSessionsManager;
import us.smartmc.core.randomwar.manager.GameTemplatesManager;
import us.smartmc.core.randomwar.manager.PlayersManager;
import us.smartmc.core.randomwar.messages.GameMessages;

public final class RandomBattle extends JavaPlugin {

    @Getter
    private static RandomBattle plugin = RandomBattle.getPlugin(RandomBattle.class);

    @Getter
    private static final PlayersManager playersManager = new PlayersManager();
    @Getter
    private static final GameTemplatesManager templatesManager = new GameTemplatesManager();
    @Getter
    private static final GameSessionsManager gameSessionManager = new GameSessionsManager();
    @Getter
    private static final GameMapManager mapsManager = new GameMapManager();

    @Getter
    private MainPluginConfig mainConfig;


    @Override
    public void onEnable() {
        plugin = this;
        plugin.getDataFolder().mkdirs();
        registerListeners();
        registerCommands();

        mainConfig = new MainPluginConfig();

        EnumMessagesRegistry.registerLanguageHolder(GameMessages.class);

        ItemActionsManager.registerCommand("lobbyHotbar", new LobbyHotbarCommand());
        ItemActionsManager.registerCommand("playerOption", new PlayerOptionCommand());
        ItemActionsManager.registerCommand("adminEditor", new AdminEditorCommand());

        registerDefaultInstances();
    }

    @Override
    public void onDisable() {
        playersManager.unload();
        templatesManager.unload();
        gameSessionManager.unload();
        mapsManager.unload();
    }

    private void registerDefaultInstances() {
        if (templatesManager.keySet().isEmpty()) {
            GameTemplate.get("default");
        }
    }

    private void registerListeners() {
        registerEvents(
                new EssentialsListeners(),
                new MainGameListeners(),
                new PlayerLogicListeners());
    }

    private void registerCommands() {
        registerCommand("adminGame", new AdminGameCommand());
        registerCommand("leave", new LeaveCommand());
    }

    private RandomBattle registerCommand(String cmdName, CommandExecutor executor) {
        PluginCommand command = getCommand(cmdName);
        if (command == null) {
            System.out.println("Error while trying to register command " + cmdName);
            return this;
        }
        command.setExecutor(executor);
        return this;
    }

    private static void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }

}

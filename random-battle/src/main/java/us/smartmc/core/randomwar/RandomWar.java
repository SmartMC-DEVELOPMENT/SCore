package us.smartmc.core.randomwar;

import lombok.Getter;
import me.imsergioh.pluginsapi.language.EnumMessagesRegistry;
import me.imsergioh.pluginsapi.language.MultiLanguageRegistry;
import me.imsergioh.pluginsapi.message.LanguageMessages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.core.randomwar.command.AdminGameCommand;
import us.smartmc.core.randomwar.command.LeaveCommand;
import us.smartmc.core.randomwar.config.MainPluginConfig;
import us.smartmc.core.randomwar.listener.EssentialListeners;
import us.smartmc.core.randomwar.manager.GameMapManager;
import us.smartmc.core.randomwar.manager.GameSessionsManager;
import us.smartmc.core.randomwar.manager.GameTemplatesManager;
import us.smartmc.core.randomwar.manager.PlayersManager;
import us.smartmc.core.randomwar.messages.GameMessages;

public final class RandomWar extends JavaPlugin {

    @Getter
    private static RandomWar plugin = RandomWar.getPlugin(RandomWar.class);

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
    }

    @Override
    public void onDisable() {
        playersManager.unload();
        templatesManager.unload();
        gameSessionManager.unload();
        mapsManager.unload();
    }

    private void registerListeners() {
        registerEvents(new EssentialListeners());
    }

    private void registerCommands() {
        registerCommand("adminGame", new AdminGameCommand());
        registerCommand("leave", new LeaveCommand());
    }

    private RandomWar registerCommand(String cmdName, CommandExecutor executor) {
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

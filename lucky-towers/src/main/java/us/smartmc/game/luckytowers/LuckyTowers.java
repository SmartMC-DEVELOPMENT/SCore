package us.smartmc.game.luckytowers;

import lombok.Getter;
import me.imsergioh.pluginsapi.instance.manager.ManagerRegistry;
import me.imsergioh.pluginsapi.language.EnumMessagesRegistry;
import me.imsergioh.pluginsapi.manager.ItemActionsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.game.luckytowers.command.AdminGameCommand;
import us.smartmc.game.luckytowers.command.LeaveCommand;
import us.smartmc.game.luckytowers.config.MainPluginConfig;
import us.smartmc.game.luckytowers.instance.game.GameTemplate;
import us.smartmc.game.luckytowers.itemcmd.AdminEditorCommand;
import us.smartmc.game.luckytowers.itemcmd.LobbyHotbarCommand;
import us.smartmc.game.luckytowers.itemcmd.PlayerOptionCommand;
import us.smartmc.game.luckytowers.listener.EssentialsListeners;
import us.smartmc.game.luckytowers.listener.MainGameListeners;
import us.smartmc.game.luckytowers.listener.PlayerLogicListeners;
import us.smartmc.game.luckytowers.manager.GameMapManager;
import us.smartmc.game.luckytowers.manager.GameSessionsManager;
import us.smartmc.game.luckytowers.manager.GameTemplatesManager;
import us.smartmc.game.luckytowers.manager.PlayersManager;
import us.smartmc.game.luckytowers.messages.GameMessages;

import java.util.HashMap;
import java.util.Map;

public final class LuckyTowers extends JavaPlugin {

    @Getter
    private static LuckyTowers plugin;

    private static final Map<String, ManagerRegistry<?, ?>> managers = new HashMap<>();

    @Getter
    private MainPluginConfig mainConfig;

    @Override
    public void onEnable() {
        plugin = this;
        plugin.getDataFolder().mkdirs();
        registerListeners();
        registerCommands();

        mainConfig = new MainPluginConfig();

        registerManagers(
                GameMapManager.class,
                GameSessionsManager.class,
                GameTemplatesManager.class,
                PlayersManager.class);

        EnumMessagesRegistry.registerLanguageHolder(GameMessages.class);

        ItemActionsManager.registerCommand("lobbyHotbar", new LobbyHotbarCommand());
        ItemActionsManager.registerCommand("playerOption", new PlayerOptionCommand());
        ItemActionsManager.registerCommand("adminEditor", new AdminEditorCommand());

        registerDefaultInstances();
    }

    @Override
    public void onDisable() {
        unloadAllManagers();
    }

    private void unloadAllManagers() {
        for (ManagerRegistry<?, ?> manager : managers.values()) {
            manager.unload();
        }
    }

    public static <V extends ManagerRegistry<?, ?>> V getManager(Class<? extends ManagerRegistry<?, ?>> clazz) {
        return (V) managers.get(clazz.getName());
    }

    @SafeVarargs
    private void registerManagers(Class<? extends ManagerRegistry<?, ?>>... classes) {
        for (Class<? extends ManagerRegistry<?, ?>> clazz : classes) {
            try {
                ManagerRegistry<?, ?> manager = clazz.newInstance();
                managers.put(clazz.getName(), manager);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void registerDefaultInstances() {
        if (getManager(GameTemplatesManager.class).keySet().isEmpty()) {
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

    private LuckyTowers registerCommand(String cmdName, CommandExecutor executor) {
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

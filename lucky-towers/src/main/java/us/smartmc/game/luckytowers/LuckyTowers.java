package us.smartmc.game.luckytowers;

import lombok.Getter;
import me.imsergioh.pluginsapi.handler.VariablesHandler;
import me.imsergioh.pluginsapi.instance.manager.ManagerRegistry;
import me.imsergioh.pluginsapi.language.EnumMessagesRegistry;
import me.imsergioh.pluginsapi.manager.ItemActionsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.core.SmartCore;
import us.smartmc.game.luckytowers.command.AdminGameCommand;
import us.smartmc.game.luckytowers.command.LeaveCommand;
import us.smartmc.game.luckytowers.config.MainPluginConfig;
import us.smartmc.game.luckytowers.event.GamePluginEvent;
import us.smartmc.game.luckytowers.instance.game.GameTemplate;
import us.smartmc.game.luckytowers.instance.player.PlayerScoreboardType;
import us.smartmc.game.luckytowers.itemcmd.AdminEditorCommand;
import us.smartmc.game.luckytowers.itemcmd.LobbyHotbarCommand;
import us.smartmc.game.luckytowers.itemcmd.PlayerOptionCommand;
import us.smartmc.game.luckytowers.itemcmd.SpectatorModeCommand;
import us.smartmc.game.luckytowers.listener.EditorListeners;
import us.smartmc.game.luckytowers.listener.EssentialsListeners;
import us.smartmc.game.luckytowers.listener.MainGameListeners;
import us.smartmc.game.luckytowers.listener.PlayerLogicListeners;
import us.smartmc.game.luckytowers.manager.*;
import us.smartmc.game.luckytowers.messages.AdminItems;
import us.smartmc.game.luckytowers.messages.AdminMessages;
import us.smartmc.game.luckytowers.messages.GameItems;
import us.smartmc.game.luckytowers.messages.GameMessages;
import us.smartmc.game.luckytowers.variable.GameVariables;
import us.smartmc.game.luckytowers.variable.PlayerStatsVariables;

import java.util.*;
import java.util.stream.Collectors;

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

        mainConfig.registerDefault("lobby-worlds", List.of("lobby", "spawn", "hub"));

        registerManagers(
                EditorModeManager.class,
                GameTemplatesManager.class,
                GameMapManager.class,
                GameSessionsManager.class,
                PlayersManager.class);

        EnumMessagesRegistry.registerLanguageHolder(
                GameMessages.class,
                AdminMessages.class,
                GameItems.class,
                AdminItems.class);

        ItemActionsManager.registerCommand("lobbyHotbar", new LobbyHotbarCommand());
        ItemActionsManager.registerCommand("playerOption", new PlayerOptionCommand());
        ItemActionsManager.registerCommand("adminEditor", new AdminEditorCommand());
        ItemActionsManager.registerCommand("spectatorMode", new SpectatorModeCommand());

        registerDefaultInstances();
        registerPlayerScoreboards();

        VariablesHandler.register(new GameVariables());
        VariablesHandler.register(new PlayerStatsVariables());
    }

    @Override
    public void onDisable() {
        unloadAllManagers();
    }

    private void registerPlayerScoreboards() {
        for (PlayerScoreboardType type : PlayerScoreboardType.values()) {
            SmartCore.getPlugin().getScoreboardHandler().register(type.getId());
        }
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
                manager.load();
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
                new PlayerLogicListeners(),
                new EditorListeners());
    }

    private void registerCommands() {
        registerCommand("adminGame", new AdminGameCommand());
        registerCommand("leave", new LeaveCommand());
    }

    public Collection<String> getLobbyWorldNames() {
        return new HashSet<>(mainConfig.getList("lobby-worlds", String.class));
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

    public static void callEvent(GamePluginEvent event) {
        Bukkit.getScheduler().runTask(LuckyTowers.getPlugin(), () -> {
            Bukkit.getPluginManager().callEvent(event);
        });
    }

}

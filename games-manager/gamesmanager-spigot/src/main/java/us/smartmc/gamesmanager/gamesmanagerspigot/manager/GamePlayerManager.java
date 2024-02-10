package us.smartmc.gamesmanager.gamesmanagerspigot.manager;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.player.GamePlayerLoadEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.player.GamePlayerLoadedEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.player.GamePlayerUnloadEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.event.player.GamePlayerUnloadedEvent;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;
import us.smartmc.gamesmanager.gamesmanagerspigot.listener.PlayerListeners;
import us.smartmc.gamesmanager.gamesmanagerspigot.util.BukkitUtil;

import java.util.HashMap;
import java.util.UUID;

public abstract class GamePlayerManager<T extends GamePlayer> implements IGamePlayerManager {

    @Getter
    private final JavaPlugin plugin;
    private final HashMap<UUID, T> players = new HashMap<>();

    public GamePlayerManager(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(new PlayerListeners(this), plugin);
    }

    public void register(T player) {
        BukkitUtil.callEvent(new GamePlayerLoadEvent(player));
        players.put(player.getUUID(), player);
        player.load();
        BukkitUtil.callEvent(new GamePlayerLoadedEvent(player));
    }

    public void unregister(UUID uuid) {
        GamePlayer gamePlayer = get(uuid);
        BukkitUtil.callEvent(new GamePlayerUnloadEvent(gamePlayer));
        get(uuid).unload();
        players.remove(uuid);
        BukkitUtil.callEvent(new GamePlayerUnloadedEvent(gamePlayer));
    }

    public T get(UUID uuid) {
        return players.get(uuid);
    }
}

package us.smartmc.snowgames.manager;

import org.bukkit.Bukkit;
import us.smartmc.gamesmanager.gamesmanagerspigot.GamesManagerAPI;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GamePlayerManager;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.player.FFAPlayer;

import java.util.UUID;

public class FFAPlayerManager<T extends FFAPlayer> extends GamePlayerManager<T> {

    public static final FFAPlayerManager<?> INSTANCE = new FFAPlayerManager<>(FFAPlugin.getFFAPlugin());

    public FFAPlayerManager(GamesManagerAPI<?, ?> plugin) {
        super(plugin);
        registerListeners();
    }

    @Override
    public FFAPlayer createGamePlayer(UUID uuid) {
        return new FFAPlayer(this, Bukkit.getPlayer(uuid));
    }
}

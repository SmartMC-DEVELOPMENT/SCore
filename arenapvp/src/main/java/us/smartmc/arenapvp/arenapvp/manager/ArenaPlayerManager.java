package us.smartmc.arenapvp.arenapvp.manager;

import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.arenapvp.arenapvp.instance.player.ArenaPlayer;
import us.smartmc.gamesmanager.gamesmanagerspigot.GamesManagerAPI;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GamePlayerManager;

import java.util.UUID;

public class ArenaPlayerManager extends GamePlayerManager<ArenaPlayer> {

    public ArenaPlayerManager(GamesManagerAPI plugin) {
        super(plugin);
    }

    @Override
    public <T extends GamePlayer> T createGamePlayer(UUID uuid) {
        return (T) new ArenaPlayer(uuid);
    }
}

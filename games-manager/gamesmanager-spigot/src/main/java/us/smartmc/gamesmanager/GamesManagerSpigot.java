package us.smartmc.gamesmanager;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.gamesmanager.game.GamePreset;
import us.smartmc.gamesmanager.manager.GameMapManager;
import us.smartmc.gamesmanager.message.GameMessages;
import us.smartmc.gamesmanager.player.GamePlayer;
import us.smartmc.gamesmanager.player.GamePlayerRepository;
import us.smartmc.gamesmanager.util.WorldUtils;

public class GamesManagerSpigot extends JavaPlugin {

    @Getter
    private static GamesManagerSpigot plugin;

    @Override
    public void onEnable() {
        plugin = this;
        getDataFolder().mkdirs();
        new GameMessages();
        GameMapManager.setupDefaults();
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            try {
                GamePlayerRepository.remove(player.getUniqueId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        WorldUtils.deleteAllTempWorlds();
    }
}
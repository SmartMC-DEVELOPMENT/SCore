package us.smartmc.test;

import me.imsergioh.pluginsapi.handler.VariablesHandler;
import me.imsergioh.pluginsapi.instance.IVariableListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.gamescore.event.player.GamePlayerJoinEvent;
import us.smartmc.gamescore.instance.PluginScoreboard;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.instance.player.PlayerScoreboard;
import us.smartmc.gamescore.manager.ScoreboardsManager;

public class TestGameImplementation extends JavaPlugin implements Listener {

    private static ScoreboardsManager scoreboardsManager;
    private static PluginScoreboard mainScoreboard;

    @Override
    public void onEnable() {

        // Integrate Games-Core
        new GameIntegration(this);
        Bukkit.getPluginManager().registerEvents(this, this);

        scoreboardsManager = MapManager.getManager(ScoreboardsManager.class);
        mainScoreboard = scoreboardsManager.getOrCreate("main_ES");

        mainScoreboard.load();

        // Player name test variable
        VariablesHandler.register(new IVariableListener<Player>() {
            @Override
            public String parse(String s) {
                return s;
            }

            @Override
            public String parse(Player player, String s) {
                if (s.contains("<player_name>")) {
                    s = s.replace("<player_name>", player.getName());
                }
                return s;
            }
        });
    }

    @EventHandler
    public void onGamePlayerJoin(GamePlayerJoinEvent event) {
        new PlayerScoreboard(event.getBukkitPlayer(), mainScoreboard);
        event.getBukkitPlayer().sendMessage("Se ha puesto o intentando por lo menos poner scoreboard");
    }
}
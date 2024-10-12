package us.smartmc.test;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.gamescore.cmd.RegionsCommand;
import us.smartmc.gamescore.cmd.SchemsCommand;
import us.smartmc.gamescore.cmd.WandCommand;
import us.smartmc.gamescore.event.player.GamePlayerJoinEvent;
import us.smartmc.gamescore.instance.PluginScoreboard;
import us.smartmc.gamescore.instance.game.team.ColorGameTeam;
import us.smartmc.gamescore.instance.game.team.ColorGameTeamColor;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.instance.player.PlayerScoreboard;
import us.smartmc.gamescore.listener.PlayerRegionSelectionListeners;
import us.smartmc.gamescore.manager.GamesManager;
import us.smartmc.gamescore.manager.GenericGameTeamsManager;
import us.smartmc.gamescore.manager.ScoreboardsManager;
import us.smartmc.gamescore.menu.EditMapInventoryMenu;
import us.smartmc.test.cmd.LeaveCommand;
import us.smartmc.test.cmd.PasteRegionCommand;
import us.smartmc.test.game.TestGame;

public class TestGameImplementation extends JavaPlugin implements Listener {

    private static TestGame mainGame;

    private static GamesManager gamesManager;

    private static PluginScoreboard mainScoreboard;

    @Override
    public void onEnable() {

        getDataFolder().mkdirs();

        // Integrate Games-Core
        new GameIntegration(this);
        Bukkit.getPluginManager().registerEvents(this, this);

        ScoreboardsManager scoreboardsManager = MapManager.getManager(ScoreboardsManager.class);
        scoreboardsManager.registerMultiLanguage("main");

        gamesManager = MapManager.getManager(GamesManager.class);

        mainGame = new TestGame();
        gamesManager.put(mainGame.getSessionId(), mainGame);

        mainScoreboard = scoreboardsManager.get("main_ES");
        mainScoreboard.load();

        PlayerRegionSelectionListeners.register();

        new RegionsCommand();
        new WandCommand();
        new SchemsCommand();
        new PasteRegionCommand();
        new LeaveCommand();

        ColorGameTeam redTeam = new ColorGameTeam(ColorGameTeamColor.RED);
        MapManager.getManager(GenericGameTeamsManager.class).put(redTeam.getName(), redTeam);
        ColorGameTeam blueTeam = new ColorGameTeam(ColorGameTeamColor.BLUE);
        MapManager.getManager(GenericGameTeamsManager.class).put(blueTeam.getName(), blueTeam);

        MapManager.getManager(GenericGameTeamsManager.class).setAllyDamageAllowed(true);
    }

    @EventHandler
    public void onGamePlayerJoin(GamePlayerJoinEvent event) {
        new PlayerScoreboard(event.getBukkitPlayer(), mainScoreboard);
        gamesManager.get(TestGame.getMainGame().getSessionId()).joinPlayer(event.getCorePlayer());
    }

    public static GamesManager getGamesManager() {
        return gamesManager;
    }
}
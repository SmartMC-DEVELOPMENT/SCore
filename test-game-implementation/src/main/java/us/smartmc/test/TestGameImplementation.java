package us.smartmc.test;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.*;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.smartmc.gamescore.cmd.RegionsCommand;
import us.smartmc.gamescore.cmd.SchemsCommand;
import us.smartmc.gamescore.cmd.WandCommand;
import us.smartmc.gamescore.event.player.GamePlayerJoinEvent;
import us.smartmc.gamescore.instance.PluginScoreboard;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.instance.player.PlayerScoreboard;
import us.smartmc.gamescore.listener.PlayerRegionSelectionListeners;
import us.smartmc.gamescore.manager.ScoreboardsManager;
import us.smartmc.test.cmd.PasteRegionCommand;

public class TestGameImplementation extends JavaPlugin implements Listener {

    private static ScoreboardsManager scoreboardsManager;
    private static PluginScoreboard mainScoreboard;

    @Override
    public void onEnable() {

        getDataFolder().mkdirs();

        // Integrate Games-Core
        new GameIntegration(this);
        Bukkit.getPluginManager().registerEvents(this, this);

        scoreboardsManager = MapManager.getManager(ScoreboardsManager.class);
        scoreboardsManager.registerMultiLanguage("main");

        mainScoreboard = scoreboardsManager.get("main_ES");
        mainScoreboard.load();

        PlayerRegionSelectionListeners.register();

        new RegionsCommand();
        new WandCommand();
        new SchemsCommand();
        new PasteRegionCommand();
    }

    @EventHandler
    public void onGamePlayerJoin(GamePlayerJoinEvent event) {
        new PlayerScoreboard(event.getBukkitPlayer(), mainScoreboard);
        event.getBukkitPlayer().sendMessage("Se ha puesto o intentando por lo menos poner scoreboard");
    }
}
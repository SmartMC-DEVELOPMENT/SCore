package us.smartmc.core.handler;

import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import me.imsergioh.pluginsapi.event.PlayerDataLoadedEvent;
import me.imsergioh.pluginsapi.event.PlayerLanguageChangedEvent;
import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.Language;
import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import us.smartmc.core.instance.PluginScoreboard;
import us.smartmc.core.instance.player.SmartCorePlayer;

import java.util.HashMap;

public class ScoreboardHandler implements Listener {

    private final HashMap<String, PluginScoreboard> scoreboards = new HashMap<>();

    public ScoreboardHandler() {
        register("main");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerDataLoadedEvent event) {
        if (event.getPlayer() == null) return;
        register(event.getPlayer(), "main");
    }

    @EventHandler
    public void onLanguageChange(PlayerLanguageChangedEvent event) {
        if (event.getCorePlayer().get() == null) return;
        register(event.getCorePlayer().get(), "main");
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        unregister(player);
    }

    public void register(String name) {
        SyncUtil.sync(() -> {
            for (Language language : Language.values()) {
                String scoreboardName = name + "_" + language.name().toUpperCase();
                scoreboards.put(scoreboardName, new PluginScoreboard(scoreboardName));
            }
        });
    }

    public void register(Player player, String name) {
        unregister(player);
        get(name, PlayerLanguages.get(player.getUniqueId())).register(player);
    }

    public void unregister(Player player) {
        BPlayerBoard board = Netherboard.instance().getBoard(player);
        if (board != null) board.delete();

        for (PluginScoreboard scoreboard : scoreboards.values()) {
            scoreboard.unregister(player);
        }
    }

    public void unregister(String name, Language language) {
        scoreboards.remove(name + "_" + language.name().toUpperCase());
    }

    public PluginScoreboard get(String name, Language language) {
        return scoreboards.get(name + "_" + language.name().toUpperCase());
    }
}

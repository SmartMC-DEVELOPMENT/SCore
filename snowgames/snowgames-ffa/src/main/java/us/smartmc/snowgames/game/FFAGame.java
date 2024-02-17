package us.smartmc.snowgames.game;

import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GamePreset;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.player.GamePlayer;
import us.smartmc.gamesmanager.gamesmanagerspigot.manager.GameManager;
import us.smartmc.snowgames.FFAPlugin;
import us.smartmc.snowgames.inventory.GameHotbar;
import us.smartmc.snowgames.inventory.LobbyHotbar;
import us.smartmc.snowgames.util.DebugUtil;

import java.util.UUID;

public class FFAGame extends GameInstance {

    public FFAGame(GameManager<?> manager, GamePreset instance) {
        super(manager, instance.getName() + "-" + UUID.randomUUID());
        map = new FFAMap("ffa_china");
        map.setMaxPlayers(Bukkit.getMaxPlayers());
    }

    @Override
    public void joinPlayer(GamePlayer player) {
        if (isInGame(player)) return;

        players.add(player);
        GameHotbar.give(player.getPlayer());

        player.getPlayer().setGameMode(GameMode.SURVIVAL);
    }

    public void setMap(FFAMap map) {
        this.map = map;
    }

    public boolean isInGame(GamePlayer gamePlayer) {
        DebugUtil.debug(getClass().getSimpleName(), "isInGame " + players);
        return players.contains(gamePlayer);
    }

    public Location getSpawn() {
        return ((FFAMap) map).getSpawn();
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    @Override
    public void quitPlayer(GamePlayer player) {
        DebugUtil.debug(getClass().getSimpleName(), "quitPlayer start");
        if (!isInGame(player)) return;
        SyncUtil.later(() -> {
            if (!player.getPlayer().isOnline()) return;
            player.getPlayer().teleport(getSpawn());
            LobbyHotbar.give(player.getPlayer());
            player.getPlayer().setGameMode(GameMode.ADVENTURE);
            Bukkit.getScheduler().runTaskLater(FFAPlugin.getFFAPlugin(), () -> {
                players.remove(player);
            }, 10);
        }, 25);
        DebugUtil.debug(getClass().getSimpleName(), "quitPlayer end");
    }

    public FFAMap getMap() {
        return (FFAMap) map;
    }

}
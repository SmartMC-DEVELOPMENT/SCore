package us.smartmc.snowgames.game;

import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import us.smartmc.core.handler.SpawnHandler;
import us.smartmc.gamesmanager.game.GamePreset;
import us.smartmc.gamesmanager.game.GameSession;
import us.smartmc.gamesmanager.game.map.GameMap;
import us.smartmc.gamesmanager.game.map.GameMapSession;
import us.smartmc.gamesmanager.player.GamePlayer;
import us.smartmc.snowgames.inventory.GameHotbar;
import us.smartmc.snowgames.inventory.LobbyHotbar;

public class FFAGame extends GameSession {

    public FFAGame(GamePreset instance) {
        super(instance);
        map = new GameMap("ffa-main");
        map.setMaxPlayers(Bukkit.getMaxPlayers());
        mapSession = new GameMapSession(map);
    }

    @Override
    public void joinPlayer(GamePlayer player) {
        if (isInGame(player.getPlayer())) return;

        players.add(player.getUuid());
        GameHotbar.give(player.getPlayer());
    }

    @Override
    public void quitPlayer(GamePlayer player) {
        if (!isInGame(player.getPlayer())) return;
        players.remove(player.getUuid());

        SyncUtil.sync(() -> {
            if (!player.getPlayer().isOnline()) return;
            player.getPlayer().teleport(getSpawn());
            LobbyHotbar.give(player.getPlayer());
        });
    }

    @Override
    public boolean canPlayerJoin(GamePlayer player) {
        if (players.size() >= map.getMaxPlayers()) return false;
        return getSpawn() != null;
    }

    public boolean isInGame(Player player) {
        return players.contains(player.getUniqueId());
    }

    public Location getSpawn() {
        return SpawnHandler.getLocation();
    }

    public GameMap getMap() {
        return map;
    }

}
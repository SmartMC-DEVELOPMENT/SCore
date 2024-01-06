package us.smartmc.snowgames.game;

import me.imsergioh.pluginsapi.util.SyncUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import us.smartmc.gamesmanager.game.GamePreset;
import us.smartmc.gamesmanager.game.GameSession;
import us.smartmc.gamesmanager.game.map.GameMapSession;
import us.smartmc.gamesmanager.player.GamePlayer;
import us.smartmc.snowgames.inventory.GameHotbar;
import us.smartmc.snowgames.inventory.LobbyHotbar;

public class FFAGame extends GameSession {

    public FFAGame(GamePreset instance) {
        super(instance);
        map = new FFAMap("ffa-main");
        map.setMaxPlayers(Bukkit.getMaxPlayers());
        mapSession = new GameMapSession(map);
    }

    @Override
    public void joinPlayer(GamePlayer player) {
        if (isInGame(player.getPlayer())) return;

        players.add(player.getUuid());
        GameHotbar.give(player.getPlayer());

        player.getPlayer().setGameMode(GameMode.SURVIVAL);
    }

    @Override
    public void quitPlayer(GamePlayer player) {
        if (!isInGame(player.getPlayer())) return;
        players.remove(player.getUuid());

        SyncUtil.later(() -> {
            if (!player.getPlayer().isOnline()) return;
            player.getPlayer().teleport(getSpawn());
            LobbyHotbar.give(player.getPlayer());

            player.getPlayer().setGameMode(GameMode.ADVENTURE);
        }, 25);
    }

    @Override
    public boolean canPlayerJoin(GamePlayer player) {
        if (players.size() >= map.getMaxPlayers()) return false;
        return getSpawn() != null;
    }

    public void setMap(FFAMap map) {
        this.map = map;
    }

    public boolean isInGame(Player player) {
        return players.contains(player.getUniqueId());
    }

    public Location getSpawn() {
        return ((FFAMap) map).getSpawn();
    }

    public FFAMap getMap() {
        return (FFAMap) map;
    }

}
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

        players.put(player.getUUID(), player);
        GameHotbar.give(player.getPlayer());

        player.getPlayer().setGameMode(GameMode.SURVIVAL);
    }

    public void setMap(FFAMap map) {
        this.map = map;
    }

    public boolean isInGame(Player player) {
        return players.containsKey(player.getUniqueId());
    }

    public boolean isInGame(GamePlayer gamePlayer) {
        return players.containsKey(gamePlayer.getUUID());
    }

    public Location getSpawn() {
        return ((FFAMap) map).getSpawn();
    }

    @Override
    public void load() {

    }

    @Override
    public void unload() {
        forEachPlayer(this::quitPlayer);
    }

    @Override
    public void quitPlayer(GamePlayer player) {
        if (!isInGame(player)) return;
        SyncUtil.later(() -> {
            if (!player.getPlayer().isOnline()) return;
            player.getPlayer().teleport(getSpawn());
            LobbyHotbar.give(player.getPlayer());
            player.getPlayer().setGameMode(GameMode.ADVENTURE);
        }, 25);

        Bukkit.getScheduler().runTaskLater(FFAPlugin.getFFAPlugin(), () -> {
            players.remove(player.getUUID());
            player.setGameInstance(null);
        }, 10);
    }

    public FFAMap getMap() {
        return (FFAMap) map;
    }

}
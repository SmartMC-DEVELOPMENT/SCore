package us.smartmc.snowgames.game;

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
import us.smartmc.snowgames.manager.ItemCooldownManager;
import us.smartmc.snowgames.messages.PluginMessages;

public class FFAGame extends GameSession {

    public FFAGame(GamePreset instance) {
        super(instance);
        map = new GameMap("ffa-main");
        map.setMaxPlayers(Bukkit.getMaxPlayers());
        mapSession = new GameMapSession(map);
    }

    @Override
    public void joinPlayer(GamePlayer player) {
        if (!canPlayerJoin(player)) {
            player.sendLanguageMessage(PluginMessages.NAME, "error_cant_join");
            return;
        }
        players.add(player.getUuid());
        GameHotbar.give(player.getPlayer());
    }

    @Override
    public void deathPlayer(GamePlayer player) {
        Player bPlayer = player.getPlayer();
        bPlayer.spigot().respawn();
        bPlayer.teleport(getSpawn());
        players.remove(player.getUuid());

        // Cancel & complete all tasks
        ItemCooldownManager.from(player.getPlayer()).cancelAll(true);

        LobbyHotbar.give(bPlayer);
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

}
package us.smartmc.gamescore.instance.game;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.joml.Vector3i;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboid;
import us.smartmc.gamescore.instance.game.map.GameMapSession;
import us.smartmc.gamescore.instance.game.team.GameTeam;
import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.instance.player.PlayerStatus;
import us.smartmc.gamescore.instance.timer.CountdownTimer;
import us.smartmc.gamescore.manager.map.GameMapSessionsManager;
import us.smartmc.gamescore.manager.team.GameSessionTeamsManager;
import us.smartmc.gamescore.manager.GamesManager;
import us.smartmc.gamescore.util.BukkitUtil;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Condition;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface IGame {

    UUID getSessionId();

    CountdownTimer getStartTimer();
    CountdownTimer getEndTimer();

    default void startWithCountdown() {
        if (getStartTimer() == null) {
            start();
            return;
        }
        getStartTimer().start();
    }

    default void endWithCountdown() {
        if (getEndTimer() == null) {
            end();
            return;
        }
        getEndTimer().start();
    }


    boolean isMapPasted();

    default Location pasteMap(World world) {return pasteMap(world, a->{});}
    Location pasteMap(World world, Consumer<BukkitCuboid> completeAction);

    void clearMapRegion();

    void start();
    void end();

    void setStatus(GameStatus status);
    GameStatus getStatus();

    void joinPlayer(GameCorePlayer player);
    void joinSpectatorViewer(GameCorePlayer player);

    void forEachPlayer(Consumer<GameCorePlayer> consumer);

    default void broadcastSound(Sound sound, float pitch, float volume) {
        forEachPlayer(gamePlayer -> {
            Player player = gamePlayer.getBukkitPlayer();
            player.playSound(player.getLocation(), sound, pitch, volume);
        });
    }

    default void broadcastMessage(String message, Object... args) {
        forEachPlayer(gamePlayer -> {
           gamePlayer.getBukkitPlayer().sendMessage(ChatUtil.parse(gamePlayer.getBukkitPlayer(), message, args));
        });
    }

    default void broadcastTitle(String title, String subtitle, Object... args) {
        forEachPlayer(gamePlayer -> {
            String parsedTitle = ChatUtil.parse(gamePlayer.getBukkitPlayer(), title, args);
            String parsedSubtitle = ChatUtil.parse(gamePlayer.getBukkitPlayer(), subtitle, args);
            gamePlayer.getBukkitPlayer().sendTitle(parsedTitle, parsedSubtitle);
        });
    }

    default void broadcastActionbar(String message, Object... args) {
        forEachPlayer(gamePlayer -> {
            String parsedMessage = ChatUtil.parse(gamePlayer.getBukkitPlayer(), message, args);
            BukkitUtil.sendActionBar(gamePlayer.getBukkitPlayer(), parsedMessage);
        });
    }

    void killPlayer(GameCorePlayer player);

    void leavePlayer(GameCorePlayer player);

    default Set<GameCorePlayer> getSpectators() {
        Set<GameCorePlayer> players = getPlayersByStatus(PlayerStatus.SPECTATOR_VIEWER);
        players.addAll(getPlayersByStatus(PlayerStatus.SPECTATOR_DEATH));
        return players;
    }

    void teleportToSpawn(Player player, GameTeam team);
    Vector3i getSpawnRelativePosition(GameTeam team);

    Set<GameCorePlayer> getPlayersByStatus(PlayerStatus status);
    Set<UUID> getPlayers();

    GameMapSession getGameMapSession();
    GameMapSessionsManager getGameMapSessionManager();
    GameSessionTeamsManager getGameSessionTeamsManager();

    default GamesManager getManager() {
        return GamesManager.getManager(GamesManager.class);
    }

}

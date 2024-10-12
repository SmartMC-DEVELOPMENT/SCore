package us.smartmc.gamescore.instance.game;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.joml.Vector3i;
import us.smartmc.gamescore.event.game.*;
import us.smartmc.gamescore.event.player.GamePlayerGameJoinEvent;
import us.smartmc.gamescore.event.player.GamePlayerGameLeaveEvent;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboid;
import us.smartmc.gamescore.instance.game.map.GameMap;
import us.smartmc.gamescore.instance.game.map.GameMapSession;
import us.smartmc.gamescore.instance.game.map.spawn.ListSpawnsHolder;
import us.smartmc.gamescore.instance.game.map.spawn.OneSpawnHolder;
import us.smartmc.gamescore.instance.game.map.spawn.RegionSpawnHolder;
import us.smartmc.gamescore.instance.game.team.GameTeam;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.instance.player.PlayerStatus;
import us.smartmc.gamescore.instance.timer.CountdownTimer;
import us.smartmc.gamescore.manager.map.GameMapSessionsManager;
import us.smartmc.gamescore.manager.team.GameSessionTeamsManager;
import us.smartmc.gamescore.util.BukkitUtil;
import us.smartmc.gamescore.util.CuboidUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class Game implements IGame {

    // Identifier for the game session instance (4 the future we can use that for save histories, last games, etc...)
    @Getter
    private final UUID sessionId;

    protected GameStatus status = GameStatus.WAITING;

    @Getter
    protected CountdownTimer startTimer, endTimer;

    @Getter
    protected final Set<UUID> players = new HashSet<>();

    private final GameSessionTeamsManager teamsManager = new GameSessionTeamsManager();

    // Create constructor
    public Game() {
        this(UUID.randomUUID());
    }

    // Main constructor (UUID impl.)
    public Game(UUID uuid) {
        this.sessionId = uuid;
    }

    @Override
    public void start() {
        BukkitUtil.callEvent(new GamePreStartEvent(this));

        // Perform after 0,5s for call post start event
        BukkitUtil.runLater(() -> {
            BukkitUtil.callEvent(new GamePostStartEvent(this));
        }, 10);
        setStatus(GameStatus.PLAYING);
        forEachPlayer(gamePlayer -> gamePlayer.setStatus(PlayerStatus.IN_GAME));
    }

    @Override
    public void end() {
        BukkitUtil.callEvent(new GamePreEndEvent(this));

        // Perform after 0,5s for call post end event
        BukkitUtil.runLater(() -> {
            BukkitUtil.callEvent(new GamePostEndEvent(this));
        }, 10);
    }

    @Override
    public void joinPlayer(GameCorePlayer player) {
        if (players.contains(player.getUUID())) return;
        players.add(player.getUUID());
        player.setCurrentGame(this);
        player.setStatus(PlayerStatus.PRE_GAME);
        BukkitUtil.callEvent(new GamePlayerGameJoinEvent(player));
    }

    @Override
    public void joinSpectatorViewer(GameCorePlayer player) {
        player.setStatus(PlayerStatus.SPECTATOR_VIEWER);
    }

    @Override
    public void leavePlayer(GameCorePlayer player) {
        players.remove(player.getUUID());
        teamsManager.remove(player.getUUID());
        player.setCurrentGame(null);
        BukkitUtil.callEvent(new GamePlayerGameLeaveEvent(player));
        BukkitUtil.getPlayer(player.getUUID()).ifPresent(p -> player.setStatus(PlayerStatus.LOBBY));
    }

    @Override
    public void killPlayer(GameCorePlayer player) {
        joinSpectatorViewer(player);
        player.setStatus(PlayerStatus.SPECTATOR_DEATH);
    }

    @Override
    public void forEachPlayer(Consumer<GameCorePlayer> consumer) {
        for (UUID id : getPlayers()) {
            consumer.accept(GameCorePlayer.of(id));
        }
    }

    @Override
    public Set<GameCorePlayer> getPlayersByStatus(PlayerStatus status) {
        Set<GameCorePlayer> list = new HashSet<>();
        for (UUID id : getPlayers()) {
            GameCorePlayer gamePlayer = GameCorePlayer.of(id);
            if (gamePlayer.getStatus().equals(status)) list.add(gamePlayer);
        }
        return list;
    }

    @Override
    public void setStatus(GameStatus status) {
        GameStatus previousStatus = this.status;
        this.status = status;
        BukkitUtil.callEvent(new GameStatusChangeEvent(this, previousStatus, status));
    }

    @Override
    public GameStatus getStatus() {
        return status;
    }

    @Override
    public void teleportToSpawn(Player player, GameTeam team) {
        BukkitCuboid cuboid = getGameMapSession().getCuboidReference();
        Vector3i position = getSpawnRelativePosition(team);
        Location loc = cuboid.getGlobalLocation(position).add(0.5, 0, 0.5);
        player.teleport(loc);
        player.sendMessage("TELEPORTING TO " + loc);
    }

    @Override
    public Vector3i getSpawnRelativePosition(GameTeam team) {
        GameMap map = getGameMapSession().getMap();
        Object spawnsHolder = map.getData().getSpawnsData().getHolder();
        Vector3i position = null;
        BukkitCuboid cuboid = getGameMapSession().getCuboidReference();
        if (spawnsHolder instanceof OneSpawnHolder holder) {
            position = holder.getRelativePosition(team, BukkitCuboid.locToIntVector(cuboid.getMinLocation()));
        }
        if (spawnsHolder instanceof RegionSpawnHolder holder) {
            position = holder.getNextRelativePosition(team, BukkitCuboid.locToIntVector(cuboid.getMinLocation()));
        }
        if (spawnsHolder instanceof ListSpawnsHolder holder) {
            position = holder.getRelativePosition(team, 0, BukkitCuboid.locToIntVector(cuboid.getMinLocation()));
        }
        return position;
    }

    @Override
    public boolean isMapPasted() {
        return getGameMapSession().getXReferenceGrid() != -1;
    }

    @Override
    public void clearMapRegion() {
        BukkitCuboid cuboid = getGameMapSession().getCuboidReference();
        if (cuboid == null) return;
        CuboidUtil.forEachBlock(cuboid, block -> {
            if (block.getType().equals(Material.AIR)) return;
            block.setType(Material.AIR);
        });
        getGameMapSession().liberateReferenceLocationIfSet();
    }

    @Override
    public Location pasteMap(World world, Consumer<BukkitCuboid> completeAction) {
        getGameMapSession().whenPastedRegion(completeAction);
        return getGameMapSession().pasteAtReferenceGridLocPoint(world);
    }

    @Override
    public GameMapSession getGameMapSession() {
        return getGameMapSessionManager().get(getSessionId());
    }

    @Override
    public GameMapSessionsManager getGameMapSessionManager() {
        return MapManager.getManager(GameMapSessionsManager.class);
    }

    @Override
    public GameSessionTeamsManager getGameSessionTeamsManager() {
        return teamsManager;
    }
}

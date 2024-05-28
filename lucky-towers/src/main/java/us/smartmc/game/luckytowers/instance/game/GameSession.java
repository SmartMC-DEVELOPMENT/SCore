package us.smartmc.game.luckytowers.instance.game;

import com.sk89q.worldedit.EditSession;
import lombok.Getter;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.util.PaperChatUtil;
import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;
import us.smartmc.game.luckytowers.LuckyTowers;
import us.smartmc.game.luckytowers.command.LeaveCommand;
import us.smartmc.game.luckytowers.event.GameStatusChangeEvent;
import us.smartmc.game.luckytowers.event.player.GamePlayerDeathEvent;
import us.smartmc.game.luckytowers.event.player.GamePlayerJoinSessionEvent;
import us.smartmc.game.luckytowers.instance.map.MapsGeneration;
import us.smartmc.game.luckytowers.instance.player.GamePlayer;
import us.smartmc.game.luckytowers.instance.player.PlayerStatus;
import us.smartmc.game.luckytowers.manager.GameMapManager;
import us.smartmc.game.luckytowers.manager.GameSessionsManager;
import us.smartmc.game.luckytowers.messages.GameMessages;
import us.smartmc.game.luckytowers.util.GameUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class GameSession implements IGameSession {

    private static final int GENERATION_ITEMS_TICKS = 60;
    private static final int DEFAULT_SECONDS_COOLDOWN = 5;

    private static final GameMapManager mapManager = LuckyTowers.getManager(GameMapManager.class);

    private final UUID id;

    private final GameMap map;

    private final Set<GamePlayer> players = new HashSet<>();

    @Getter
    private final GameSessionTeams teams;

    @Getter
    private GameSessionStatus status = GameSessionStatus.WAITING;

    private int secondsRemaining, playersRemaining;
    private BukkitRunnable timeLimitTask, generateItemsTask;

    private int referenceXChunkReserved;
    private int xAddition = -1;

    private EditSession schemSession;

    @Getter
    private int countdown;
    private BukkitRunnable startRunnable;

    public GameSession(UUID id, GameMap map) {
        this.id = id;
        this.map = map;
        this.teams = new GameSessionTeams(this);
    }

    private void loadMapSchemAndReserveChunks() {
        if (xAddition != -1) return;
        MapsGeneration generation = GameMapManager.getMainMapsGeneration();
        Chunk chunk = generation.reserveNext();
        xAddition = MapsGeneration.getXAdditionByChunk(map.getSpawn(getMapsWorld(), 0).getChunk(), chunk);
        referenceXChunkReserved = chunk.getX();
        schemSession = MapsGeneration.loadAndPasteSchematic(getMapsWorld(), this);
        if (schemSession == null) end();
    }

    @Override
    public void start() {
        if (getStatus().equals(GameSessionStatus.STARTING)) return;
        setStatus(GameSessionStatus.STARTING);
        forEachPlayer(gamePlayer -> {
            gamePlayer.setStatus(PlayerStatus.INGAME);
            gamePlayer.getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
        });
        playersRemaining = getAlivePlayers().size();
        teams.forEachTeam(team -> {
            team.getPlayers().forEach(uuid -> {
                GamePlayer gamePlayer = GamePlayer.get(uuid);
                gamePlayer.onlinePlayer(p -> {
                    p.teleport(team.getSpawnAssigned(xAddition));
                });
            });
        });
        countdown = DEFAULT_SECONDS_COOLDOWN;
        startRunnable = new BukkitRunnable() {
            float soundPitch = 0.0f;
            @Override
            public void run() {
                if (countdown <= 0) {
                    broadcastMessage(GameMessages.session_message_started);
                    getGenerateItemsTask().runTaskTimerAsynchronously(LuckyTowers.getPlugin(), GENERATION_ITEMS_TICKS, GENERATION_ITEMS_TICKS);
                    getTimeLimitTask().runTaskTimerAsynchronously(LuckyTowers.getPlugin(), 0, 20);
                    setStatus(GameSessionStatus.PLAYING);

                    // Update status to INGAME (Scoreboard update)
                    forEachOnlinePlayer(p -> gamePlayer(p).setStatus(PlayerStatus.INGAME));
                    cancel();
                }
                if (countdown >= 1) {
                    broadcastActionbar(GameMessages.session_actionBar_startingIn, countdown);
                }
                countdown--;
                soundPitch += 0.2f;
            }
        };
        startRunnable.runTaskTimerAsynchronously(LuckyTowers.getPlugin(), 0, 20);
    }

    @Override
    public void end() {
        if (getStatus().equals(GameSessionStatus.ENDING)) return;

        setStatus(GameSessionStatus.ENDING);

        try {
            getTimeLimitTask().cancel();
        } catch (IllegalStateException ignore) {}

        try {
            getGenerateItemsTask().cancel();
        } catch (IllegalStateException ignore) {}

        GamePlayer winner = getAlivePlayers().isEmpty() ? null : getAlivePlayers().stream().toList().get(0);
        if (winner != null) winner.addWin();

        GameUtil.removeAllEntitiesInRegion(getMapsWorld(), this);
        schemSession.undo(schemSession);
        GameMapManager.getMainMapsGeneration().setAvailable(referenceXChunkReserved);
        new HashSet<>(players).forEach(gamePlayer -> {
            gamePlayer.onlinePlayer(LeaveCommand::leave);
        });
        GameSessionsManager manager = LuckyTowers.getManager(GameSessionsManager.class);
        manager.unregister(id);
    }

    public void checkStartCancellation() {
        if (status != GameSessionStatus.STARTING) return;
        if (canStart()) return;
        cancelStart();
    }

    public void cancelStart() {
        forEachOnlinePlayer(player -> {
            PaperChatUtil.send(player, GameMessages.session_cancelled);
        });
        startRunnable.cancel();
    }

    @Override
    public boolean canStart() {
        return teams.getTeamsWithPlayersSize() >= map.getTemplate().getMinTeamSize();
    }

    @Override
    public boolean canEnd() {
        if (secondsRemaining <= 0) return true;
        return playersRemaining <= 1;
    }

    @Override
    public boolean canPlayersJoin(int amount) {
        if (status.equals(GameSessionStatus.PLAYING)) return false;
        if (status.equals(GameSessionStatus.ENDING)) return false;
        return teams.getFreeSlots() >= amount;
    }

    @Override
    public void joinPlayer(GamePlayer gamePlayer) {
        gamePlayer.setGameSession(this);
        loadMapSchemAndReserveChunks();
        players.add(gamePlayer);
        gamePlayer.onlinePlayer(p -> {
            p.teleport(map.getSpawn(getMapsWorld(), xAddition));
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5f, 2f);
            p.setGameMode(GameMode.ADVENTURE);
        });
        gamePlayer.setStatus(PlayerStatus.INGAME);
        teams.assignNextEmptyTeam(gamePlayer);
        LuckyTowers.callEvent(new GamePlayerJoinSessionEvent(gamePlayer));
        if (canStart()) start();
    }

    @Override
    public void quitPlayer(GamePlayer gamePlayer) {
        playersRemaining--;
        teams.clearTeams(gamePlayer);
        players.remove(gamePlayer);
        gamePlayer.setStatus(PlayerStatus.LOBBY);
        checkStartCancellation();
        if (canEnd()) end();
        gamePlayer.setGameSession(null);
    }

    @Override
    public void deathPlayer(GamePlayer gamePlayer) {
        playersRemaining--;
        gamePlayer.setStatus(PlayerStatus.SPECTATING);
        LuckyTowers.callEvent(new GamePlayerDeathEvent(gamePlayer));
        gamePlayer.onlinePlayer(player -> {
            Location location = player.getLocation();
            player.playSound(location, Sound.ENTITY_PLAYER_DEATH, 1.0f, 2.0f);

            if (player.isDead())
                player.spigot().respawn();
            player.teleport(map.getSpawn(getMapsWorld(), xAddition));
            player.setGameMode(GameMode.ADVENTURE);
        });
        if (canEnd()) end();
    }

    @Override
    public void setStatus(GameSessionStatus status) {
        this.status = status;
        LuckyTowers.callEvent(new GameStatusChangeEvent(this, status));
    }

    @Override
    public Set<GamePlayer> getAlivePlayers() {
        return getPlayers().stream().filter(gamePlayer -> gamePlayer.getStatus().equals(PlayerStatus.INGAME)).collect(Collectors.toSet());
    }

    public BukkitRunnable getGenerateItemsTask() {
        if (generateItemsTask != null) return generateItemsTask;
        generateItemsTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (getStatus().equals(GameSessionStatus.ENDING)) cancel();
                getAlivePlayers().forEach(gamePlayer -> {
                    gamePlayer.onlinePlayer(player -> {
                        player.getInventory().addItem(ItemBuilder.of(GameUtil.getRandomMaterial()).get());
                        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 1f);
                    });
                });
            }
        };
        return generateItemsTask;
    }

    public BukkitRunnable getTimeLimitTask() {
        if (timeLimitTask != null) return timeLimitTask;
        secondsRemaining = map.getTimeLimit();
        timeLimitTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (secondsRemaining <= 0) {
                    end();
                    cancel();
                }
                secondsRemaining--;
            }
        };
        return timeLimitTask;
    }

    private static World getMapsWorld() {
        return Bukkit.getWorld("maps");
    }

}

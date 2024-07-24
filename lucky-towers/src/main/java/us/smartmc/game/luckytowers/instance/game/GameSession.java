package us.smartmc.game.luckytowers.instance.game;

import com.sk89q.worldedit.EditSession;
import lombok.Getter;
import me.imsergioh.pluginsapi.instance.item.ItemBuilder;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.*;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
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

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class GameSession implements IGameSession {

    private static final int GENERATION_ITEMS_TICKS = 60;
    private static final int DEFAULT_SECONDS_COOLDOWN = 30;
    private static final int SECONDS_COOLDOWN_HALF_MAP = 30;

    private static final GameMapManager mapManager = LuckyTowers.getManager(GameMapManager.class);

    private final UUID id;

    private final GameMap map;

    private final Set<GamePlayer> players = new HashSet<>();

    @Getter
    private int spectatorsCount;

    @Getter
    private final GameSessionTeams teams;

    @Getter
    private GameSessionStatus status = GameSessionStatus.WAITING;

    private int secondsRemaining = 10;
    private BukkitRunnable timeLimitTask, generateItemsTask;

    private int referenceXChunkReserved;
    private int xAddition = -1;

    @Getter
    private boolean startedRecently;

    private EditSession schemSession;

    @Getter
    private int countdown;
    private BukkitRunnable startRunnable;

    private long currentItemTicks;
    private BossBar announceNewItemBar;

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

    public void forceStart() {
        if (!status.equals(GameSessionStatus.STARTING)) return;
        countdown = 3;
    }

    @Override
    public void start() {
        if (getStatus().equals(GameSessionStatus.STARTING)) return;
        setStatus(GameSessionStatus.STARTING);
        forEachPlayer(gamePlayer -> {
            gamePlayer.setStatus(PlayerStatus.INGAME);
            gamePlayer.getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
        });
        broadcastSound(Sound.ENTITY_ENDER_DRAGON_HURT, 1f, 0.5f);

        countdown = DEFAULT_SECONDS_COOLDOWN;
        startRunnable = new BukkitRunnable() {
            float soundPitch = 0.0f;
            @Override
            public void run() {
                if (countdown <= 0) {
                    broadcastMessage(GameMessages.session_message_started);
                    getGenerateItemsTask().runTaskTimerAsynchronously(LuckyTowers.getPlugin(), 0, 1);
                    getTimeLimitTask().runTaskTimerAsynchronously(LuckyTowers.getPlugin(), 0, 20);
                    setStatus(GameSessionStatus.PLAYING);
                    startedRecently = true;

                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            startedRecently = false;
                        }
                    }, 1000 * 3);

                    // Update status to INGAME (Scoreboard update)
                    forEachOnlinePlayer(p -> gamePlayer(p).setStatus(PlayerStatus.INGAME));

                    Bukkit.getScheduler().runTask(LuckyTowers.getPlugin(), () -> {
                        // Teleport to spawns of teams
                        teams.forEachTeam(team -> {
                            team.getPlayers().forEach(uuid -> {
                                GamePlayer gamePlayer = GamePlayer.get(uuid);
                                gamePlayer.resetActionbar();
                                gamePlayer.resetTitle();
                                gamePlayer.onlinePlayer(p -> {
                                    p.teleport(team.getSpawnAssigned(getMapsWorld(), xAddition));
                                    p.getInventory().clear();
                                });
                            });
                        });
                    });
                    cancel();
                }
                if (countdown >= 1) {
                    broadcastActionbar(GameMessages.session_actionBar_startingIn, countdown);
                    broadcastTitle(GameMessages.starting_title, GameMessages.starting_subtitle, countdown);
                    broadcastSound(Sound.BLOCK_LAVA_POP, 1f, 0.1f / countdown);
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
        if (winner != null) {
            winner.addWin();
            WinEffectTask winEffectTask = new WinEffectTask(this, winner);
            winEffectTask.start();
            return;
        }
        resetAndEndMap();
    }

    protected void resetAndEndMap() {
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
        startRunnable.cancel();
        setStatus(GameSessionStatus.WAITING);

        forEachPlayer(gamePlayer -> {
            Player player = gamePlayer.getBukkitPlayer();
            if (player == null) return;
            PaperChatUtil.send(player, GameMessages.session_cancelled);
            GameUtil.updateScoreboard(gamePlayer);
        });
    }

    @Override
    public boolean canStart() {
        return teams.getTeamsWithPlayersSize() >= map.getTemplate().getMinTeamSize();
    }

    @Override
    public boolean canEnd() {
        if (secondsRemaining <= 0) return true;
        return getAlivePlayers().size() <= 1;
    }

    @Override
    public boolean canPlayersJoin(int amount) {
        if (status.equals(GameSessionStatus.PLAYING)) return false;
        if (status.equals(GameSessionStatus.ENDING)) return false;
        return teams.getFreeSlots() >= amount;
    }

    @Override
    public void joinPlayer(GamePlayer gamePlayer) {
        if (!canPlayersJoin(1)) return;
        gamePlayer.setGameSession(this);
        loadMapSchemAndReserveChunks();
        players.add(gamePlayer);
        gamePlayer.setStatus(PlayerStatus.INGAME);
        gamePlayer.onlinePlayer(p -> {
            p.teleport(map.getSpawn(getMapsWorld(), xAddition));
            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5f, 2f);
        });

        teams.assignNextEmptyTeam(gamePlayer);
        LuckyTowers.callEvent(new GamePlayerJoinSessionEvent(gamePlayer));
        if (canStart()) start();
    }

    @Override
    public void quitPlayer(GamePlayer gamePlayer) {
        if (gamePlayer.getStatus().equals(PlayerStatus.SPECTATING))
            spectatorsCount--;

        teams.clearTeams(gamePlayer);
        players.remove(gamePlayer);
        if (announceNewItemBar != null)
            announceNewItemBar.removePlayer(gamePlayer.getBukkitPlayer());
        gamePlayer.setStatus(PlayerStatus.LOBBY);
        gamePlayer.setGameSession(null);
        checkStartCancellation();

        // End session if gets empty and was waiting
        if (status.equals(GameSessionStatus.WAITING) && players.isEmpty()) {
            end();
        }

        if (status.equals(GameSessionStatus.PLAYING))
            if (canEnd()) end();
    }

    @Override
    public void deathPlayer(GamePlayer gamePlayer) {
        gamePlayer.setStatus(PlayerStatus.SPECTATING);
        spectatorsCount++;
        LuckyTowers.callEvent(new GamePlayerDeathEvent(gamePlayer));

        broadcastSound(Sound.ENTITY_PLAYER_DEATH, 1f, 2f);

        gamePlayer.sendTitle(GameMessages.death_title, GameMessages.death_subtitle);

        gamePlayer.onlinePlayer(player -> {
            if (player.isDead())
                player.spigot().respawn();
            player.teleport(map.getSpawn(getMapsWorld(), xAddition));
        });
        if (canEnd()) end();

        Bukkit.getScheduler().runTaskLater(LuckyTowers.getPlugin(), () -> {
            boolean hasQuit = false;
            if (gamePlayer.getBukkitPlayer() == null) {
                hasQuit = true;
            } else if (!gamePlayer.getBukkitPlayer().isOnline()) hasQuit = true;
            // Remove from set Players if the player is not online. To check end
            if (hasQuit) LeaveCommand.leave(gamePlayer.getBukkitPlayer());
        }, 1);
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

            final int maxTicks = GENERATION_ITEMS_TICKS;

            @Override
            public void run() {
                currentItemTicks++;

                // BossBar init & addition of players
                if (announceNewItemBar == null) {
                    announceNewItemBar = Bukkit.createBossBar("", BarColor.BLUE, BarStyle.SOLID);
                    forEachOnlinePlayer(player -> {
                        announceNewItemBar.addPlayer(player);
                    });
                }

                if (getStatus().equals(GameSessionStatus.ENDING)) cancel();

                double progress = (double) (GENERATION_ITEMS_TICKS - (currentItemTicks % GENERATION_ITEMS_TICKS)) / GENERATION_ITEMS_TICKS;
                announceNewItemBar.setProgress(progress);

                if (currentItemTicks % GENERATION_ITEMS_TICKS == 0) {
                    getAlivePlayers().forEach(gamePlayer -> {
                        gamePlayer.onlinePlayer(player -> {
                            player.getInventory().addItem(ItemBuilder.of(GameUtil.getRandomMaterial()).get());
                            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1f, 1f);
                        });
                    });
                }
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

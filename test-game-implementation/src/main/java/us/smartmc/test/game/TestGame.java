package us.smartmc.test.game;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.joml.Vector3i;
import us.smartmc.gamescore.instance.cuboid.BukkitCuboid;
import us.smartmc.gamescore.instance.game.Game;
import us.smartmc.gamescore.instance.game.GameStatus;
import us.smartmc.gamescore.instance.game.map.GameMap;
import us.smartmc.gamescore.instance.game.map.GameMapSession;
import us.smartmc.gamescore.instance.game.map.spawn.ListSpawnsHolder;
import us.smartmc.gamescore.instance.game.map.spawn.OneSpawnHolder;
import us.smartmc.gamescore.instance.game.map.spawn.RegionSpawnHolder;
import us.smartmc.gamescore.instance.game.team.GameTeam;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.instance.timer.CountdownTimer;
import us.smartmc.gamescore.manager.GenericGameTeamsManager;
import us.smartmc.gamescore.manager.map.GameMapSessionsManager;
import us.smartmc.gamescore.manager.map.MapsManager;
import us.smartmc.gamescore.util.BukkitUtil;
import us.smartmc.gamescore.util.CuboidUtil;
import us.smartmc.test.TestGameImplementation;

import java.util.ArrayList;

public class TestGame extends Game {

    private static final TestGame mainGame = new TestGame();

    private final World world;

    private final GameMap map;

    public TestGame() {
        super();
        world = Bukkit.getWorlds().get(0);
        TestGameImplementation.getGamesManager().put(getSessionId(), this);
        startTimer = new CountdownTimer(timer -> {
            if (timer.getSecondsLeft() <= 0) {
                start();
                timer.performEnd();
                return;
            }
            forEachPlayer(gamePlayer -> {
                gamePlayer.getBukkitPlayer().sendMessage(ChatUtil.color("&e&lEMPEZANDO EN &c&l" + timer.getSecondsLeft() + "s&e&l..."));
            });
        }, 5000) {
        };
        MapsManager manager = MapManager.getManager(MapsManager.class);
        map = manager.get("test_38");
        GameMapSessionsManager sessionsManager = MapManager.getManager(GameMapSessionsManager.class);
        sessionsManager.register(this, map);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void joinPlayer(GameCorePlayer player) {
        GameMapSessionsManager sessionsManager = MapManager.getManager(GameMapSessionsManager.class);
        GameMapSession session = sessionsManager.get(getSessionId());

        // Alternate team from blue to red
        String teamName = players.size() % 2 == 0 ? "blue" : "red";
        getGameSessionTeamsManager().put(player.getUUID(), teamName);

        Object spawnsHolder = map.getData().getSpawnsData().getHolder();
        GenericGameTeamsManager teamsManager = MapManager.getManager(GenericGameTeamsManager.class);


        if (session.getXReferenceGrid() == -1) {
            session.whenPastedRegion(cuboid -> {
                player.getBukkitPlayer().sendMessage("PASTING2 " + spawnsHolder.getClass().getName());
                GameTeam team = teamsManager.get(teamName);
                // One
                if (spawnsHolder instanceof OneSpawnHolder oneHolder) {
                    Vector3i position = oneHolder.getRelativePosition(team, BukkitCuboid.locToIntVector(cuboid.getMinLocation()));
                    Location loc = cuboid.getGlobalLocation(position).add(0.5, 0, 0.5);
                    player.getBukkitPlayer().teleport(loc);
                        player.getBukkitPlayer().sendMessage("TELEPORTING TO " + loc);
                }

                // Region
                if (spawnsHolder instanceof RegionSpawnHolder regionHolder) {
                    Vector3i position = regionHolder.getNextRelativePosition(team, BukkitCuboid.locToIntVector(cuboid.getMinLocation()));
                    Location loc = cuboid.getGlobalLocation(position).add(0.5, 0, 0.5);
                    player.getBukkitPlayer().teleport(loc);
                        player.getBukkitPlayer().sendMessage("TELEPORTING TO " + loc);
                }

                // List
                if (spawnsHolder instanceof ListSpawnsHolder listHolder) {
                    Vector3i position = listHolder.getRelativePosition(team, 0, BukkitCuboid.locToIntVector(cuboid.getMinLocation()));
                    Location loc = cuboid.getGlobalLocation(position).add(0.5, 0, 0.5);
                    player.getBukkitPlayer().teleport(loc);
                        player.getBukkitPlayer().sendMessage("TELEPORTING TO " + loc);
                }
            });
            sessionsManager.get(getSessionId()).pasteAtReferenceGridLocPoint(Bukkit.getWorlds().get(0));
        } else {
            BukkitCuboid cuboid = session.getCuboidReference();
            GameTeam team = teamsManager.get(teamName);
            player.getBukkitPlayer().sendMessage("ALREADY PASTED " + spawnsHolder.getClass().getName());
            // One
            if (spawnsHolder instanceof OneSpawnHolder oneHolder) {
                Vector3i position = oneHolder.getRelativePosition(team, BukkitCuboid.locToIntVector(cuboid.getMinLocation()));
                Location loc = cuboid.getGlobalLocation(position).add(0.5, 0, 0.5);
                player.getBukkitPlayer().teleport(loc);
                player.getBukkitPlayer().sendMessage("TELEPORTING TO " + loc);
            }

            // Region
            if (spawnsHolder instanceof RegionSpawnHolder regionHolder) {
                Vector3i position = regionHolder.getNextRelativePosition(team, BukkitCuboid.locToIntVector(cuboid.getMinLocation()));
                Location loc = cuboid.getGlobalLocation(position).add(0.5, 0, 0.5);
                player.getBukkitPlayer().teleport(loc);
                player.getBukkitPlayer().sendMessage("TELEPORTING TO " + loc);
            }

            // List
            if (spawnsHolder instanceof ListSpawnsHolder listHolder) {
                Vector3i position = listHolder.getRelativePosition(team, 0, BukkitCuboid.locToIntVector(cuboid.getMinLocation()));
                Location loc = cuboid.getGlobalLocation(position).add(0.5, 0, 0.5);
                player.getBukkitPlayer().teleport(loc);
                player.getBukkitPlayer().sendMessage("TELEPORTING TO " + loc);
            }

        }

        if (status.equals(GameStatus.PLAYING)) {
            joinSpectatorViewer(player);
            return;
        }
        super.joinPlayer(player);

        forEachPlayer(gamePlayer -> {
            gamePlayer.getBukkitPlayer().sendMessage(ChatUtil.color("&ePlayer &b" + player.getBukkitPlayer().getName() + "&e has joined the game!"));
        });

        if (players.size() >= 2)
            startWithCountdown();
    }

    @Override
    public void leavePlayer(GameCorePlayer gamePlayer) {
        super.leavePlayer(gamePlayer);
        BukkitUtil.getPlayer(gamePlayer.getUUID()).ifPresent(player -> {
            player.sendMessage("Poes te has salido crack del juego");
        });
        if (players.size() == 1) {
            GameCorePlayer winner = GameCorePlayer.of(new ArrayList<>(players).get(0));
            Bukkit.broadcastMessage("WINNER! = " + winner);

            // Restablecer mapa ->
            GameMapSessionsManager sessionsManager = MapManager.getManager(GameMapSessionsManager.class);
            GameMapSession session = sessionsManager.get(getSessionId());

            CuboidUtil.forEachBlock(session.getCuboidReference(), block -> {
                if (block.getType().equals(Material.AIR)) return;
                block.setType(Material.AIR);
            });
            session.liberateReferenceLocationIfSet();
        }
    }

    public static TestGame getMainGame() {
        return mainGame;
    }
}

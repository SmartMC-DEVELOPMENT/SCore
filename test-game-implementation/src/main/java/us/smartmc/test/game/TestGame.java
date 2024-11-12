package us.smartmc.test.game;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import us.smartmc.gamescore.instance.game.Game;
import us.smartmc.gamescore.instance.game.GameStatus;
import us.smartmc.gamescore.instance.game.WaitingLobbySession;
import us.smartmc.gamescore.instance.game.map.GameMap;
import us.smartmc.gamescore.instance.game.map.GameMapSession;
import us.smartmc.gamescore.instance.manager.MapManager;
import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.instance.timer.CountdownTimer;
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

    private WaitingLobbySession waitingLobbySession;

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
        map = manager.getOrCreate("test_31");
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

        // Alternate team from blue to red
        String teamName = players.size() % 2 == 0 ? "blue" : "red";
        getGameSessionTeamsManager().put(player.getUUID(), teamName);


        if (!getMainGame().isMapPasted()) {
            player.getBukkitPlayer().sendMessage("Pasting...");
            pasteMap(Bukkit.getWorlds().get(0), cuboid -> {
                player.getBukkitPlayer().sendMessage("Pasted!");
                Location waitingLobbyLoc = getWaitingLobbyLocation(Bukkit.getWorlds().get(0), cuboid, 10);

                waitingLobbySession = new WaitingLobbySession("old", this);
                waitingLobbySession.pasteRegionAt(waitingLobbyLoc);


                new Thread(() -> {
                    Location location = waitingLobbySession.getSpawn();
                    while (location == null) {
                        location = waitingLobbySession.getSpawn();
                        System.out.println("Checking variable spawn");
                    }

                    player.getBukkitPlayer().teleport(location);
                    player.getBukkitPlayer().sendMessage("Teleported to " + location.toString());
                }).start();


            });
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

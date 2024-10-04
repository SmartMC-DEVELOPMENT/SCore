package us.smartmc.test.game;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import us.smartmc.gamescore.instance.game.Game;
import us.smartmc.gamescore.instance.game.GameStatus;
import us.smartmc.gamescore.instance.game.team.GameTeam;
import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.instance.timer.CountdownTimer;
import us.smartmc.gamescore.util.BukkitUtil;
import us.smartmc.test.TestGameImplementation;

public class TestGame extends Game {

    private static final TestGame mainGame = new TestGame();

    private final World world;

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
            players.forEach(gameCorePlayer -> {
                gameCorePlayer.getBukkitPlayer().sendMessage(ChatUtil.color("&e&lEMPEZANDO EN &c&l" + timer.getSecondsLeft() + "s&e&l..."));
            });
        }, 5000){};
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void joinPlayer(GameCorePlayer player) {
        if (status.equals(GameStatus.PLAYING)) {
            joinSpectatorViewer(player);
            return;
        }
        super.joinPlayer(player);
        player.getBukkitPlayer().teleport(world.getSpawnLocation());

        players.forEach(gameCorePlayer -> {
            gameCorePlayer.getBukkitPlayer().sendMessage(ChatUtil.color("&ePlayer &b" + player.getBukkitPlayer().getName() + "&e has joined the game!"));
        });

        //String teamName = players.size() % 2 == 0 ? "blue" : "red";

        getGameSessionTeamsManager().put(player.getUUID(), "red");

        if (players.size() >= 2)
            startWithCountdown();
    }

    @Override
    public void leavePlayer(GameCorePlayer gamePlayer) {
        super.leavePlayer(gamePlayer);
        BukkitUtil.getPlayer(gamePlayer.getUUID()).ifPresent(player -> {
            player.sendMessage("Poes te has salido crack del juego");
        });
    }

    public static TestGame getMainGame() {
        return mainGame;
    }
}

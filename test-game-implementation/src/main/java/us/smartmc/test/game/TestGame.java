package us.smartmc.test.game;

import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import us.smartmc.gamescore.instance.game.Game;
import us.smartmc.gamescore.instance.player.GameCorePlayer;
import us.smartmc.gamescore.util.BukkitUtil;
import us.smartmc.test.TestGameImplementation;

public class TestGame extends Game {

    private static final TestGame mainGame = new TestGame();

    private final World world;

    public TestGame() {
        super();
        world = Bukkit.getWorlds().get(0);
        TestGameImplementation.getGamesManager().put(getSessionId(), this);
    }

    @Override
    public void joinPlayer(GameCorePlayer player) {
        super.joinPlayer(player);
        player.getBukkitPlayer().teleport(world.getSpawnLocation());

        players.forEach(gameCorePlayer -> {
            gameCorePlayer.getBukkitPlayer().sendMessage(ChatUtil.color("&ePlayer &b" + player.getBukkitPlayer().getName() + "&e has joined the game!"));
        });
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

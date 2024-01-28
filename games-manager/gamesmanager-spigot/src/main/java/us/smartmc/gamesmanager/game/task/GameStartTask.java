package us.smartmc.gamesmanager.game.task;

import lombok.Getter;
import me.imsergioh.pluginsapi.util.ChatUtil;
import org.bukkit.entity.Player;
import us.smartmc.gamesmanager.game.GameStatus;
import us.smartmc.gamesmanager.game.IGameSession;
import us.smartmc.gamesmanager.message.GameMessages;

import java.util.concurrent.TimeUnit;

@Getter
public class GameStartTask extends GameRepeatingTask {

    private final int seconds;

    public GameStartTask(IGameSession<Player> gameSession, int seconds) {
        super(gameSession, 0, 1, TimeUnit.SECONDS);
        this.seconds = seconds;

        setTask(() -> {
            gameSession.setStatus(GameStatus.STARTING);
            if (seconds == 0) {
                gameSession.broadcast(GameMessages.getMessageVariable("game_started"));
                gameSession.start();
                shutdown();
            }

            if (seconds >= 1) {
                gameSession.forEachGamePlayer(gamePlayer -> {
                    gamePlayer.sendMessage(ChatUtil.parse(gamePlayer, "Game starting in {0}!", seconds));
                });
            }
        });
    }
}

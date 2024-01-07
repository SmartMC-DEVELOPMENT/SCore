package us.smartmc.gamesmanager.game.task;

import lombok.Getter;
import us.smartmc.gamesmanager.game.GameStatus;
import us.smartmc.gamesmanager.game.IGameSession;
import us.smartmc.gamesmanager.message.GameMessages;
import us.smartmc.gamesmanager.player.GamePlayer;

import java.util.concurrent.TimeUnit;

@Getter
public class GameStartTask extends GameRepeatingTask {

    private final int seconds;

    public GameStartTask(IGameSession<GamePlayer> gameSession, int seconds) {
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
                    gamePlayer.sendMessage("Game starting in {0}!", seconds);
                });
            }
        });
    }
}

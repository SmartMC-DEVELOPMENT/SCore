package us.smartmc.gamesmanager.game.task;

import us.smartmc.gamesmanager.game.IGameSession;

import java.util.concurrent.TimeUnit;

public class GameRepeatingTask extends GameTask {

    private final long delay, period;
    private final TimeUnit unit;

    public GameRepeatingTask(IGameSession session, long delay, int period, TimeUnit unit) {
        super(session);
        this.delay = delay;
        this.period = period;
        this.unit = unit;
    }

    public void start() {
        executorService.scheduleAtFixedRate(task, delay, period, unit);
    }

}

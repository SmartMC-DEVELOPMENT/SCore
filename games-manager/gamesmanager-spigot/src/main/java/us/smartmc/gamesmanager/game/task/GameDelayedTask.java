package us.smartmc.gamesmanager.game.task;

import us.smartmc.gamesmanager.game.IGameSession;

import java.util.concurrent.TimeUnit;

public class GameDelayedTask extends GameTask {

    private long additionalDelay;

    public GameDelayedTask(IGameSession session, Runnable task, long delay, TimeUnit unit) {
        super(session);
        setTask(task);
        executorService.schedule(getRunnable(), delay, unit);
    }

    public void addDelay(long delay, TimeUnit unit) {
        additionalDelay += unit.toMillis(delay);
    }

    private Runnable getRunnable() {
        return () -> {
            if (additionalDelay >= 1) {
                try {
                    Thread.sleep(additionalDelay);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            task.run();
            shutdown();
        };
    }

}

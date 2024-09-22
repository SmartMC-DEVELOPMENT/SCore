package us.smartmc.gamescore.instance.timer;

import us.smartmc.gamescore.util.BukkitUtil;

import java.util.TimerTask;
import java.util.function.Consumer;

public class Timer implements ITimer {

    private final Timer instance;
    private final java.util.Timer timer = new java.util.Timer();
    private final TimerTask task;
    private final Consumer<Timer> timerConsumer;

    public Timer(Consumer<Timer> timerConsumer) {
        this.instance = this;
        this.timerConsumer = timerConsumer;
        this.task = new TimerTask() {
            @Override
            public void run() {
                getRunnable().run();
            }
        };
    }

    public void performLater(long delay) {
        timer.schedule(task, delay);
    }

    public void repeatingTask(long delay, long period) {
        timer.schedule(task, delay, period);
    }

    @Override
    public void stop() {
        timer.cancel();
    }

    protected Runnable getRunnable() {
        return () -> {
            BukkitUtil.runSync(() -> {
                timerConsumer.accept(instance);
            });
        };
    }
}

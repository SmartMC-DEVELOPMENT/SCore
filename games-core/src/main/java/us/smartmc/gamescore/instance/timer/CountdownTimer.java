package us.smartmc.gamescore.instance.timer;

import lombok.Getter;
import us.smartmc.gamescore.util.BukkitUtil;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Getter
public abstract class CountdownTimer extends Timer implements ICountdownTimer {

    private final CountdownTimer instance;
    private final Consumer<ICountdownTimer> timerConsumer;
    private final long countdown;
    protected long start, end;

    public CountdownTimer(Consumer<ICountdownTimer> timerConsumer, TimeUnit unit, long timeAmount) {
        this(timerConsumer, unit.toMillis(timeAmount));
    }

    public CountdownTimer(Consumer<ICountdownTimer> timerConsumer, long countdown) {
        super(null);
        this.instance = this;
        this.timerConsumer = timerConsumer;
        this.countdown = countdown;
    }

    @Override
    public void start() {
        this.start = System.currentTimeMillis();
        this.end = start + countdown;
        repeatingTask(0, 1000);
        performStart();
    }

    @Override
    public long getDuration() {
        return end - start;
    }

    @Override
    public long getSecondsLeft() {
        return (end - System.currentTimeMillis()) / 1000;
    }

    @Override
    public void performEnd() {
    }

    @Override
    public void performStart() {
    }

    @Override
    protected Runnable getRunnable() {
        return () -> {
            BukkitUtil.runSync(() -> {
                perform();
                timerConsumer.accept(instance);
            });
        };
    }
}

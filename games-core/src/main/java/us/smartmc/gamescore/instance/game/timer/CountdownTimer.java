package us.smartmc.gamescore.instance.game.timer;

import lombok.Getter;
import us.smartmc.gamescore.util.BukkitUtil;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Getter
public abstract class CountdownTimer extends Timer implements IGameCountdownTimer {

    private final CountdownTimer instance;
    private final Consumer<IGameCountdownTimer> timerConsumer;
    protected final long start, end;

    public CountdownTimer(Consumer<IGameCountdownTimer> timerConsumer, TimeUnit unit, long timeAmount) {
        this(timerConsumer, unit.toMillis(timeAmount));
    }

    public CountdownTimer(Consumer<IGameCountdownTimer> timerConsumer, long countdown) {
        this(timerConsumer, System.currentTimeMillis(), System.currentTimeMillis() + countdown);
    }

    public CountdownTimer(Consumer<IGameCountdownTimer> timerConsumer, long start, long end) {
        super(null);
        this.instance = this;
        this.timerConsumer = timerConsumer;
        this.start = start / 1000;
        this.end = end / 1000;
    }

    @Override
    public void start() {
        repeatingTask(0, 1000);
        performStart();
    }

    @Override
    public long getDuration() {
        return end - start;
    }

    @Override
    public long getSecondsLeft() {
        return end - (System.currentTimeMillis() / 1000);
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
                timerConsumer.accept(instance);
            });
        };
    }
}

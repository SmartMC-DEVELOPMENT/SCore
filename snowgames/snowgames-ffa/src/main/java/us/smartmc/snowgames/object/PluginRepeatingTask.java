package us.smartmc.snowgames.object;

import lombok.Getter;

import java.util.TimerTask;

public class PluginRepeatingTask extends TimerTask {

    @Getter
    protected long period, end = -1;
    protected Runnable completeRunnable, eachDelay;
    @Getter
    protected boolean active = true;

    public PluginRepeatingTask(long periodMillis) {
        this.period = periodMillis;
    }

    public PluginRepeatingTask onComplete(Runnable runnable) {
        this.completeRunnable = runnable;
        return this;
    }

    public PluginRepeatingTask onDelay(Runnable eachDelay) {
        this.eachDelay = eachDelay;
        return this;
    }

    @Override
    public void run() {
        if (end == -1) end = System.currentTimeMillis() + period;

        if (!active) return;
        if (System.currentTimeMillis() >= end) {
            if (completeRunnable != null) {
                completeRunnable.run();
            }
            active = false;
            super.cancel();
            return;
        }
        if (eachDelay != null)
            eachDelay.run();
    }

    public long getRemainingTimeInSeconds() {
        long currentTime = System.currentTimeMillis();
        long remainingTimeMillis = end - currentTime;
        return remainingTimeMillis > 0 ? remainingTimeMillis / 1000 : 0;
    }
}

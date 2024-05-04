package us.smartmc.core.luckywars.instance.task;

import java.util.Timer;
import java.util.TimerTask;

public class RepeatingTask {

    private final Timer timer;
    private final TimerTask task;

    public RepeatingTask(Runnable runnable) {
        this.timer = new Timer();
        this.task = new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    public void schedule(long delay, long period) {
        timer.schedule(task, delay, period);
    }

}

package us.smartmc.game.luckytowers.instance.task;

import java.util.Timer;
import java.util.TimerTask;

public class LaterTask {

    private final Timer timer;
    private final TimerTask task;

    public LaterTask(Runnable runnable) {
        this.timer = new Timer();
        task = new TimerTask(){
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    private void schedule(long millisLater) {
        timer.schedule(task, millisLater);
    }

    private void cancel() {
        timer.cancel();
        task.cancel();
    }

}

import us.smartmc.gamescore.instance.timer.Timer;

import java.util.concurrent.atomic.AtomicInteger;

public class TestTimers {

    public static void main(String[] args) {
        // GameCountdownTimer
        /*new CountdownTimer(t -> {
            System.out.println("LOL " + t.getSecondsLeft());
        }, TimeUnit.SECONDS, 10) {
        }.start();*/

        AtomicInteger count = new AtomicInteger();
        Timer timer = new Timer(t -> {
            System.out.println("Stopping... " + count.get());
            count.getAndIncrement();
        });
        timer.repeatingTask(0,1000);
    }

}

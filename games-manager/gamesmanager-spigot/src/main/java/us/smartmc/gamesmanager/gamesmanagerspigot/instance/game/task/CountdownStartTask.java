package us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.task;

import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameTask;

public class CountdownStartTask extends GameTask {

    private static final int DEFAULT_COUNTDOWN = 5;

    private int countdown = DEFAULT_COUNTDOWN;

    public CountdownStartTask(GameInstance instance) {
        super(instance);
    }

    @Override
    public void run() {
        if (countdown == DEFAULT_COUNTDOWN) onCountdownStart();
        onRepeat();
        if (countdown == 0) {
            onCountdownEnd();
            cancel();
        }
        countdown--;
    }

    public void onRepeat() {

    }

    public void onCountdownStart() {
    }

    public void onCountdownEnd() {
    }

}

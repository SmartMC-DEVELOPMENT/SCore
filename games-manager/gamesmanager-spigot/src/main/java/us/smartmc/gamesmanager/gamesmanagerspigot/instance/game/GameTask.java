package us.smartmc.gamesmanager.gamesmanagerspigot.instance.game;

import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class GameTask implements Runnable {

    protected final GameInstance game;
    @Setter
    private GameTaskScheduler scheduler;

    public GameTask(GameInstance instance) {
        this.game = instance;
    }

    protected void cancel() {
        if (scheduler == null) return;
        scheduler.submit(this);
    }

}

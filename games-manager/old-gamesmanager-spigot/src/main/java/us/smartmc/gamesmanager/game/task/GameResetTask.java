package us.smartmc.gamesmanager.game.task;

import us.smartmc.gamesmanager.game.IGameSession;

public class GameResetTask extends GameTask {
    protected GameResetTask(IGameSession session) {
        super(session);
    }

    @Override
    public Runnable getTask() {
        return super.getTask();
    }
}

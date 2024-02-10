package us.smartmc.arenapvp.arenapvp.instance.task;

import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.task.CountdownStartTask;

public class CountdownTask extends CountdownStartTask {

    public CountdownTask(GameInstance instance) {
        super(instance);
    }

    @Override
    public void onRepeat() {
        if (!game.canGameStart()) {
            cancel();
        }
        super.onRepeat();
    }

    @Override
    protected void cancel() {
        game.getPlayers().forEach(player -> {
            player.sendMessage("");
        });
        super.cancel();
    }

    @Override
    public void onCountdownEnd() {

    }
}

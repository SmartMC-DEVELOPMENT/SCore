package us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.task;

import me.imsergioh.pluginsapi.instance.PlayerLanguages;
import me.imsergioh.pluginsapi.language.Language;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameInstance;
import us.smartmc.gamesmanager.gamesmanagerspigot.instance.game.GameTask;
import us.smartmc.gamesmanager.gamesmanagerspigot.message.GameManagerMessages;

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
        game.getPlayers().forEach(player -> {
            Language language = PlayerLanguages.get(player.getUUID());
            String[] title = GameManagerMessages.get().getTitle(language, "starting");
            player.sendTitle(title[0], title[1], 0, 20, 0, countdown);
            player.sendMessage(GameManagerMessages.get().getChatMessage(language, "starting"), countdown);
        });
    }

    public void onCountdownStart() {
    }

    public void onCountdownEnd() {
    }

}

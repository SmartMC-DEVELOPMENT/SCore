package us.smartmc.core.randomwar.instance.game;

import lombok.Getter;
import us.smartmc.core.randomwar.instance.task.BukkitRepeatingTask;

import java.util.UUID;

public class GameSession {

    @Getter
    private UUID id;

    private GameMap map;

    private BukkitRepeatingTask itemTasks;

    private GameSession(UUID id) {
        this.id = id;
    }

}

package us.smartmc.gamesmanager.game.task;

import lombok.Getter;
import lombok.Setter;
import us.smartmc.gamesmanager.game.IGameSession;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Getter
public class GameTask {

    private static final HashMap<IGameSession, Set<GameTask>> tasks = new HashMap<>();

    protected final ScheduledExecutorService executorService;
    @Setter
    protected Runnable task;

    protected GameTask(IGameSession session) {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        getTasks(session).add(this);
    }


    public void submit() {
        submit(task);
    }

    public void submit(Runnable runnable) {
        executorService.submit(runnable);
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public static Set<GameTask> getTasks(IGameSession session) {
        if (!tasks.containsKey(session)) tasks.put(session, new HashSet<>());
        return tasks.get(session);
    }

}

package us.smartmc.gamesmanager.gamesmanagerspigot.instance.game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

@SuppressWarnings("unused")
public class GameTaskScheduler {

    private final ScheduledExecutorService service;
    private final Map<GameInstance, Set<GameTask>> tasks = new HashMap<>();

    public GameTaskScheduler(int corePoolSize) {
        service = Executors.newScheduledThreadPool(corePoolSize);
    }

    public ScheduledFuture<?> schedule(GameTask task, long delay, TimeUnit unit) {
        registerTask(task);
        return service.schedule(task, delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(GameTask task, long delay, long period, TimeUnit unit) {
        registerTask(task);
        return service.scheduleAtFixedRate(task, delay, period, unit);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(GameTask task, long delay, long period, TimeUnit unit) {
        registerTask(task);
        return service.scheduleWithFixedDelay(task, delay, period, unit);
    }

    public Future<?> cancel(GameTask task) {
        return service.submit(task);
    }

    public Future<?> submit(GameTask task) {
        unregisterTask(task);
        return service.submit(task);
    }

    public void execute(GameTask task) {
        registerTask(task);
        task.setScheduler(this);
        service.execute(task);
    }

    public Set<GameTask> getTasks(GameInstance instance) {
        return tasks.getOrDefault(instance, new HashSet<>());
    }

    private void registerTask(GameTask task) {
        Set<GameTask> list = getTasks(task.getGame());
        list.add(task);
        tasks.put(task.getGame(), list);
    }

    private void unregisterTask(GameTask task) {
        Set<GameTask> list = getTasks(task.getGame());
        list.remove(task);
        tasks.put(task.getGame(), list);
    }
}


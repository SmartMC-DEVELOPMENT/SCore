package us.smartmc.smartbot.handler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventSchedulerHandler {

    private static final Map<Long, List<Runnable>> scheduledTasks = new ConcurrentHashMap<>();

    private static TimerTask executeTask;

    public static void register(long timestamp, Runnable runnable) {
        List<Runnable> list = scheduledTasks.computeIfAbsent(timestamp, k -> new ArrayList<>());
        list.add(runnable);
        scheduledTasks.put(timestamp, list);
    }

    public static void setup() {
        executeTask = new TimerTask() {
            @Override
            public void run() {
                runTasks();
            }
        };
        new Timer().scheduleAtFixedRate(executeTask, 250, 1000);
    }

    public static void runTasks() {
        long currentTimestamp = System.currentTimeMillis() / 1000;
        for (long timestamp : scheduledTasks.keySet()) {
            if (timestamp <= currentTimestamp) {
                runTasks(timestamp);
            }
        }
    }

    public static void runTasks(long timestamp) {
        List<Runnable> runnableList = scheduledTasks.get(timestamp);
        for (Runnable runnable : runnableList) {
            runnable.run();
        }
        scheduledTasks.remove(timestamp);
    }

}

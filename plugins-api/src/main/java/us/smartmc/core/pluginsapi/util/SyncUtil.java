package us.smartmc.core.pluginsapi.util;

public class SyncUtil {

    public static void sync(Runnable runnable) {
        new Thread(() -> {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            runnable.run();
        }).start();
    }

    public static void later(Runnable runnable, long millis) {
        new Thread(() -> {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            runnable.run();
        }).start();
    }

}
